TaskFlow  
TaskFlow - это простое веб-приложение для управление пользователями и задачами. Проект реализован на Java, 
с использованием Spring Boot, Spring Data JPA, Spring Security, Hibernate и MySQL.  
  
Приложение позволяет вести учет пользователей и задач с разделением прав доступа по ролям.  
  
Функционал  
Приложение поддерживает три роли пользователей  
-ADMIN - полный контроль над системой  
-MANAGER - управление задачами  
-USER - ограниченный доступ к своим задачам  
  
Текущие возможности  
ADMIN может:  
-Регистрировать новых пользователей с валидацией данных (имя, email, пароль)  
-Удалять пользователей  
-Просматривать список всех пользователей и поиск по ID, имени или email  
ADMIN и MANAGER могут:  
-Обновлять информацию о пользователях (имя, email)  
-Создавать задачи  
-Обновлять данные задач (title, description, status, priority, executorID, deadline)  
USER может:  
-Создавать задачи  
-Просматривать только свои задачи  
-Обновлять статус своей задачи  
  
Задачи:  
-У каждой задачи есть один создатель и один исполнитель  
-У пользователя может быть несколько созданных и выполняемых задач  
-Полная реляционная связь реализована через JPA @OneToMany и @ManyToOne  
-Пагинация списков задач
-Фильтрация реализована через Specification (по status, priority, executorID, deadline)  
  
Технологии  
Java 21  
Spring Boot (REST API)  
Spring Security  
Spring Data JPA/Hibernate  
MySQL  
Maven    
  
*Планы на развитие проекта   
-Поддержка JWT-аутентификации  
-Фильтрация и сортировка задач  
  

#Установка и запуск  
1. Склонировать репозиторий:  
  bash  
  git clone https://github.com/yourusername/taskflow.git  
  cd taskflow  

3. Настроить базу данных MySQL:  
  CREATE DATABASE taskflow_db;  

5. Настроить application.properties:  
  spring.datasource.url=jdbc:mysql://localhost:3306/taskflow_db  
  spring.datasource.username=root  
  spring.datasource.password=your_password  
  spring.jpa.hibernate.ddl-auto=update  
  server.port=8080  

7. Сборка и запуск приложения:  
  mvn clean install  
  mvn spring-boot:run  
Приложение будет доступно по адресу: http://localhost:8080  

USER API  
| Метод | URL                 | Роль                    | Описание                           |  
|-------|---------------------|-------------------------|------------------------------------|  
| POST  | /api/users          | ADMIN                   | Создать пользователя               |  
| PUT   | /api/users/{id}     | ADMIN, MANAGER          | Обновить данные пользователя       |  
| GET   | /api/users          | Все роли                | Получить всех пользователей        |  
| GET   | /api/users/{id}     | Все роли                | Получить пользователя по ID        |  
| DELETE| /api/users/{id}     | ADMIN                   | Удалить пользователя               |  
| GET   | /api/users/username | Все роли                | Получить пользователя по username  |  
| GET   | /api/users/email    | Все роли                | Получить пользователя по email     |  

Запрос на создание пользователя:  
{  
        "username": "manager2",  
        "email": "manager2@gmail.com",  
        "role": "MANAGER",  
        "password" : "123456",  
        "passwordConfirm" : "123456"  
}  

Запрос на обновление пользователя:  
{  
        "username": "AZAAA",  
        "email": "rabaza@gmail.com"  
}  

TASK API  
| Метод  | URL                           | Роль                    | Описание                             |
|--------|-------------------------------|-------------------------|--------------------------------------|
| POST   | /api/tasks                    | ADMIN, MANAGER          | Создать задачу                       |
| PATCH  | /api/tasks/{id}               | Все роли                | Обновить задачу                      |
| GET    | /api/tasks                    | Все роли                | Получить все задачи                  |  
| DELETE | /api/tasks/{id}               | ADMIN, MANAGER          | Удаление задачи                      |  
| GET    | /api/tasks/{id}               | Все роли                | Поиск задачи по id                   |   
  
Запрос на создание задачи:  
{  
    "title": "test6",  
    "description": "text6",  
    "deadline": "2026-03-27T23:59:00"  
}  

Запрос на обновление данных задачи:  
{  
        "title": "test2000",  
        "description": "text2000",  
        "priority": "LOW",  
        "status": "IN_PROGRESS",  
        "executorID": 2  
}  
