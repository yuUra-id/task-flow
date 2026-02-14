package com.sharko.yura.taskflow.repository;

import com.sharko.yura.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

}
