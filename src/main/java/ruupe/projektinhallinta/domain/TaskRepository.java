package ruupe.projektinhallinta.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends CrudRepository<Task, Long> {

    @Transactional
    void deleteAllByProjectId(Long id);

}
