package com.sharko.yura.taskflow.repository;

import com.sharko.yura.taskflow.entity.Task;
import com.sharko.yura.taskflow.entity.TaskPriority;
import com.sharko.yura.taskflow.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностью Task.
 * Предоставляет методы для получения задач по разным критериям:
 * создатель, исполнитель, статус, приоритет и срок выполнения.
 * Наследуется от JpaRepository, что обеспечивает стандартные CRUD-операции.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {

    /**
     * Получить все задачи, созданные конкретным пользователем.
     */
    List<Task> findByCreatorId(Long creatorId);
    /**
     * Получить все задачи, назначенные конкретному исполнителю.
     */
    List<Task> findByExecutorId(Long executorId);
    /**
     * Получить задачи по создателю и статусу.
     */
    List<Task> findByCreatorIdAndStatus(Long creatorId, TaskStatus status);
    /**
     * Получить задачи по создателю и приоритету.
     */
    List<Task> findByCreatorIdAndPriority(Long creatorId, TaskPriority priority);
    /**
     * Получить задачи по исполнителю и приоритету.
     */
    List<Task> findByExecutorIdAndPriority(Long executorId, TaskPriority priority);
    /**
     * Получить задачи по исполнителю и статусу.
     */
    List<Task> findByExecutorIdAndStatus(Long executorId, TaskStatus status);
    /**
     * Получить задачи с конкретным сроком выполнения.
     */
    List<Task> findByDeadline(LocalDateTime deadline);

}
