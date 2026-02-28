package com.sharko.yura.taskflow.repository.specification;

import com.sharko.yura.taskflow.dto.TaskFilterDTO;
import com.sharko.yura.taskflow.entity.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public final class TaskSpecification {

    private TaskSpecification () {}

    public static Specification<Task> build(TaskFilterDTO taskFilterDTO) {

        if (taskFilterDTO == null) {
            return (root, criteriaQuery, cb) -> cb.conjunction();
        }

        return Specification
                .where(hasStatus(taskFilterDTO))
                .and(hasPriority(taskFilterDTO))
                .and(hasExecutor(taskFilterDTO))
                .and(hasDeadlineFrom(taskFilterDTO))
                .and(hasDeadlineTo(taskFilterDTO));

    }

    public static Specification<Task> hasPriority(TaskFilterDTO taskFilterDTO) {

        return (root, query, cb) -> {

            if(taskFilterDTO.getTaskPriority() == null){
                return null;
            }

            return cb.equal(root.get("priority"), taskFilterDTO.getTaskPriority());

        };

    }

    public static Specification<Task> hasExecutor(TaskFilterDTO taskFilterDTO) {

        return (root, query, cb) -> {

            if(taskFilterDTO.getExecutorId() == null){
                return null;
            }

            return cb.equal(root.get("executor").get("id"), taskFilterDTO.getExecutorId());

        };

    }

    public static Specification<Task> hasDeadlineFrom(TaskFilterDTO taskFilterDTO) {

        return (root, query, cb) -> {

            if(taskFilterDTO.getDeadlineFrom() == null){
                return null;
            }

            return cb.greaterThanOrEqualTo(root.get("deadline"), taskFilterDTO.getDeadlineFrom());

        };

    }

    public static Specification<Task> hasDeadlineTo(TaskFilterDTO taskFilterDTO) {

        return (root, query, cb) -> {

            if(taskFilterDTO.getDeadlineTo() == null){
                return null;
            }

            return cb.lessThanOrEqualTo(root.get("deadline"), taskFilterDTO.getDeadlineTo());

        };

    }

    private static Specification<Task> hasStatus(TaskFilterDTO taskFilterDTO) {

        return new Specification<Task>() {

            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cd){

                if(taskFilterDTO.getTaskStatus() == null){
                    return null;
                }

                return cd.equal(root.get("status"), taskFilterDTO.getTaskStatus());

            }

        };

    }

}
