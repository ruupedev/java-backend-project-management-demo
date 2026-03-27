package ruupe.projektinhallinta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ruupe.projektinhallinta.domain.Project;
import ruupe.projektinhallinta.domain.ProjectRepository;
import ruupe.projektinhallinta.domain.Task;

@SpringBootTest(classes = ProjektinhallintaApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void createNewProject() {
        Project project = new Project();
        project.setName("Test Project");
        projectRepository.save(project);
        assertThat(project.getName().equals("Test Project"));
    }

    @Test void createNewProjectAndTask() {
        Project project = new Project();
        project.setName("Test Project");
        projectRepository.save(project);
        Task task = new Task();
        task.setProject(project);
        project.setTasks(List.of(task));
        projectRepository.save(project);
        assertNotNull(project.getId());
        assertEquals(1, project.getTasks().size());
    }

}
