package com.sharko.yura.taskflow.repository.specification;

import com.sharko.yura.taskflow.entity.Role;
import com.sharko.yura.taskflow.entity.Task;
import com.sharko.yura.taskflow.entity.User;
import org.springframework.data.jpa.domain.Specification;

public final class TaskRoleSpecification {

    private TaskRoleSpecification() {}

    public static Specification<Task> forUser(User correntUser) {

        if(correntUser.getRole() == Role.ADMIN || correntUser.getRole() == Role.MANAGER){

            return (root, criteriaQuery, cb) ->
                    cb.conjunction();

        }
        if(correntUser.getRole() == Role.USER){
            return (root, criteriaQuery, cb) ->
                    cb.equal(root.get("executor").get("id"), correntUser.getId());
        }

        return (root, criteriaQuery, cb) ->
                cb.conjunction();

    }

}
