package com.sharko.yura.taskflow.repository;

import com.sharko.yura.taskflow.entity.Task;
import com.sharko.yura.taskflow.entity.TaskPriority;
import com.sharko.yura.taskflow.entity.TaskStatus;
import com.sharko.yura.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByCreatorId(Long creatorId);

    List<Task> findByExecutorId(Long executorId);

    List<Task> findByCreatorIdAndStatus(Long creatorId, TaskStatus status);

    List<Task> findByCreatorIdAndPriority(Long creatorId, TaskPriority priority);

    List<Task> findByExecutorIdAndPriority(Long executorId, TaskPriority priority);

    List<Task> findByExecutorIdAndStatus(Long executorId, TaskStatus status);

    List<Task> findByDeadline(LocalDateTime deadline);

}
