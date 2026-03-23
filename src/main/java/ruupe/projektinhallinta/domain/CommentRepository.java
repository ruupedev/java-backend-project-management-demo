package ruupe.projektinhallinta.domain;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Transactional
    void deleteByTaskId(Long taskId);
}
