package ruupe.projektinhallinta.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import ruupe.projektinhallinta.domain.Comment;
import ruupe.projektinhallinta.domain.CommentRepository;
import ruupe.projektinhallinta.domain.Employee;
import ruupe.projektinhallinta.domain.EmployeeRepository;
import ruupe.projektinhallinta.domain.Project;
import ruupe.projektinhallinta.domain.ProjectRepository;
import ruupe.projektinhallinta.domain.Tag;
import ruupe.projektinhallinta.domain.TagRepository;
import ruupe.projektinhallinta.domain.Task;
import ruupe.projektinhallinta.domain.TaskRepository;
import ruupe.projektinhallinta.domain.TaskStatus;



@Controller
public class MainController {

    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;


    MainController(ProjectRepository projectRepository, TaskRepository taskRepository, EmployeeRepository employeeRepository, CommentRepository commentRepository, TagRepository tagRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
    }


    /* GET MAPPING */


    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/createProject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreateProject(Model model) {
        model.addAttribute("project", new Project());
        return "createProject";
    }

    @GetMapping("/editTask/{projectId}/{taskId}")
    /* @PreAuthorize("hasAuthority('ADMIN')") */
    public String getEditTask(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
        model.addAttribute("task", taskRepository.findById(taskId).orElseThrow());
        model.addAttribute("project", projectRepository.findById(projectId).orElseThrow());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("tags", tagRepository.findAll());
        return "editTask";
    }

    @GetMapping("/changeStatus/{projectId}/{taskId}")
    /* @PreAuthorize("hasAuthority('ADMIN')") */
    public String getChangeStatus(Model model, @PathVariable Long taskId, @PathVariable Long projectId) {
        model.addAttribute("task", taskRepository.findById(taskId).orElseThrow());
        model.addAttribute("project", projectRepository.findById(projectId).orElseThrow());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("tags", tagRepository.findAll());
        return "changeStatus";
    }

    @GetMapping("/add/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdd(Model model, @PathVariable Long id) {
        model.addAttribute("task", new Task());
        model.addAttribute("project", projectRepository.findById(id).orElseThrow());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "add";
    }

    @GetMapping("/manage/{id}")
    /* @PreAuthorize("hasAuthority('ADMIN')") */
    public String getManage(Model model, @PathVariable Long id){
        model.addAttribute("project", projectRepository.findById(id).orElseThrow());
        return "manage";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        commentRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/addComment/{id}")
    public String getAddComment(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        model.addAttribute("task", task);    
        model.addAttribute("comment", new Comment());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("project", task.getProject());
        return "addComment";
    }

    /* POST MAPPING */

    @PostMapping("/saveComment")
    public String saveComment(@RequestParam Long task_id, @RequestParam Long employee_id, @RequestParam Long project_id, @RequestParam String comment) {
        Task targetTask = taskRepository.findById(task_id).orElseThrow();
        Employee employee = employeeRepository.findById(employee_id).orElseThrow();
        Comment newComment = new Comment();

        newComment.setComment(comment);
        newComment.setEmployee(employee);
        newComment.setTask(targetTask);

        commentRepository.save(newComment);

        return "redirect:/manage/" + project_id;
    }
    

    @PostMapping("/saveProject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveProject(@Valid @ModelAttribute("project") Project project, BindingResult result) {
            if (result.hasErrors()) {
        return "createProject";
    }
    projectRepository.save(project);
    return "redirect:/";
    }
    
    @PostMapping("/savetask")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveTask(
        @Valid 
        @ModelAttribute("task") Task task, 
        BindingResult result,  
        @RequestParam Long project_id, 
        @RequestParam Long employee_id,
        @RequestParam(required = false) List<Long> tags, 
        Model model) 
        {
        
         if (result.hasErrors()) {
            model.addAttribute("project", projectRepository.findById(project_id).orElseThrow());
            model.addAttribute("employees", employeeRepository.findAll());
            return "add";
        }
        
        Project project = projectRepository.findById(project_id).orElseThrow();
        task.setProject(project);
        Employee employee = employeeRepository.findById(employee_id).orElseThrow();
        task.setEmployee(employee);
        
        if (tags != null) {
            List<Tag> selectedTags = new ArrayList<>();
            tagRepository.findAllById(tags).forEach(selectedTags::add);
            task.setTags(selectedTags);
        }
      
        taskRepository.save(task);
        return "redirect:/manage/" + project_id;
    }

    @PostMapping("/saveEditedTask")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveEditedTask(
        @RequestParam Long project_id, 
        @RequestParam Long employee_id, 
        @RequestParam Long task_id, 
        @RequestParam String description,
        @RequestParam TaskStatus status, 
        @RequestParam(required = false) List<Long> tags,
        @RequestParam String title) {
        Task task = taskRepository.findById(task_id).orElseThrow();
        Employee employee = employeeRepository.findById(employee_id).orElseThrow();

        task.setTitle(title);
        task.setDescription(description);
        task.setEmployee(employee);
        task.setStatus(status);

        if (tags != null) {
            List<Tag> selectedTags = new ArrayList<>();
            tagRepository.findAllById(tags).forEach(selectedTags::add);
            task.setTags(selectedTags);
        }
        
        taskRepository.save(task);

        return "redirect:/manage/" + project_id;

    }

    @PostMapping("/saveEditedStatus")
    public String saveEditedStatus(
        @RequestParam Long project_id, 
        @RequestParam Long task_id, 
        @RequestParam TaskStatus status
        ) {
        Task task = taskRepository.findById(task_id).orElseThrow();

        task.setStatus(status);

        taskRepository.save(task);

        return "redirect:/manage/" + project_id;

    }
    
}
