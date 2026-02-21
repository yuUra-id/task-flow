package com.sharko.yura.taskflow.repository;

import com.sharko.yura.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью User.
 * Обеспечивает стандартные CRUD-операции через наследование JpaRepository
 * и предоставляет дополнительные методы для поиска пользователя по имени и email,
 * а также проверки существования пользователя в базе данных.
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
