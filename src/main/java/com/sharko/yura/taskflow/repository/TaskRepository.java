package com.sharko.yura.taskflow.repository;

import com.sharko.yura.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Репозиторий для работы с сущностью Task.
 * Наследуется от JpaRepository, что обеспечивает стандартные CRUD-операции.
 */
public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {

}
