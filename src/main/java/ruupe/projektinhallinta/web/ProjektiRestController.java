package ruupe.projektinhallinta.web;

import org.springframework.web.bind.annotation.RestController;

import ruupe.projektinhallinta.domain.CommentRepository;
import ruupe.projektinhallinta.domain.Employee;
import ruupe.projektinhallinta.domain.Project;
import ruupe.projektinhallinta.domain.ProjectRepository;
import ruupe.projektinhallinta.domain.EmployeeRepository;
import ruupe.projektinhallinta.domain.Task;
import ruupe.projektinhallinta.domain.TaskRepository;
import ruupe.projektinhallinta.domain.TaskRequest;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProjektiRestController {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public ProjektiRestController(ProjectRepository projectRepository, TaskRepository taskRepository, EmployeeRepository employeeRepository, CommentRepository commentRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("api/projects")
    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    @GetMapping("api/projects/{id}")
    public Optional<Project> findProjectById(@PathVariable Long id) {
        return projectRepository.findById(id);
    }
    
    @GetMapping("api/tasks")
    public Iterable<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("api/tasks/{id}")
    public Optional<Task> findTaskById(@PathVariable Long id) {
        return taskRepository.findById(id);
    }

    @DeleteMapping("api/projects/{id}")
    public Iterable<Project> deleteProjectById(@PathVariable Long id) {
        taskRepository.deleteAllByProjectId(id);
        projectRepository.deleteById(id);
        return projectRepository.findAll();
    }

    @DeleteMapping("api/task/{id}")
    public Iterable<Task> deleteTaskById(@PathVariable Long id) {
        commentRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
        return taskRepository.findAll();
    }

    @PostMapping("api/projects/add")
    public Project addProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @PostMapping("api/projects/addtask")
    public Task addTaskToProject(@RequestBody TaskRequest request) {
        Project project = projectRepository.findById(request.projectId).orElseThrow();
        Employee employee = employeeRepository.findById(request.employeeId).orElseThrow();
        Task task = new Task();

        task.setTitle(request.title);
        task.setDescription(request.description);
        task.setProject(project);
        task.setEmployee(employee);

        return taskRepository.save(task);

    }

    @PutMapping("api/projects/changename/{id}")
    public Project modifyProject(@PathVariable Long id, @RequestBody Project editedProject) {
        Project existing = projectRepository.findById(id).orElseThrow();
        existing.setName(editedProject.getName());
        return projectRepository.save(existing);
    }
    

    @PutMapping("api/tasks/{id}")
    public Task editTask(@RequestBody TaskRequest request, @PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        Project project = projectRepository.findById(request.projectId).orElseThrow();
        Employee employee = employeeRepository.findById(request.employeeId).orElseThrow();

        task.setTitle(request.title);
        task.setDescription(request.description);
        task.setProject(project);
        task.setEmployee(employee);
        task.setStatus(request.status);

        return taskRepository.save(task);
    }
    
}
