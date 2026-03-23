package ruupe.projektinhallinta.domain;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;


@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="Project name cannot be empty")
    private String name;

    @OneToMany(mappedBy = "project")
    private List <Task> tasks;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
}
