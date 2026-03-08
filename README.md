# TaskFlow  


*TaskFlow* — это REST-API сервис для управления пользователями и задачами с ролевой моделью доступа.  


В проекте реализовано распределение и контроль задач между пользователями в системе с разными уровнями прав.
В приложении можно создавать пользователей, назначать роли (ADMIN, MANAGER, USER) и управлять задачами,
где у каждой задачи есть создатель, исполнитель, статус, приоритет и срок выполнения.


### Система обеспечивает:
- разграничение доступа к операциям по ролям;
- назначение исполнителей и контроль статуса задач;
- хранение и управление связями между пользователями и задачами;
- поиск, фильтрацию и пагинацию задач через API.
---


## Технологии  
- Java 21
- Spring Boot 4.0.2
- Spring Security
- Spring Web (REST API)
- Spring Data JPA/Hibernate
- MySQL
- Apache Maven
- JSON Web Tokens (JWT)
---


## Установка


### 1. Склонировать репозиторий:  
```
  bash  
  git clone https://github.com/yourusername/taskflow.git  
  cd taskflow  
```


### 2. Настроить базу данных (в проекте используется MySQL):  
- #### Создайте базу данных
```
  CREATE DATABASE taskflow_db;
```
- #### Создайте пользователя (если нужно)
```
  DROP USER IF EXISTS 'STUDENT'@'LOCALHOST';
  CREATE USER 'STUDENT'@'LOCALHOST' IDENTIFIED BY 'STUDENT';
  GRANT ALL PRIVILEGES ON *.* TO 'STUDENT'@'LOCALHOST';
```


### 3. Настроить конфигурацию:  
- #### в файле application.properties
```
  spring.datasource.url=jdbc:mysql://localhost:3306/taskflow_db  
  spring.datasource.username=root  
  spring.datasource.password=your_password  
  spring.jpa.hibernate.ddl-auto=update  
  server.port=8080
```


### 4. Настроить JWT:  
- #### Проект использует JWT для аутентификации. Необходимо задать секретный ключ через переменную окружения.
##### Linux/macOS
```
  export JWT_SECRET=my-super-secret-key
```
##### Windows (PowerShell)
```
  setx JWT_SECRET "my-super-secret-key"
```


### 5. Сборка проекта:
- #### В корне проекта выполните:
##### Linux/macOS
```
  ./mvnw clean install
```
##### Windows (PowerShell)
```
  mvnw.cmd clean install
```
##### Или если установлен Maven
```
  mvn clean install
```


### 6. Запуск:
- #### Запуск через Maven
```
  ./mvnw spring-boot:run
```
или
```
  mvn spring-boot:run
```


### 7. Проверка работы:
- #### После запуска сервер будет доступен по адресу
```
  http://localhost:8080
```
---
 
  
## Использование 
### Роли и их функционал:

-    #### ADMIN:  
     Доступен весь функционал
-    #### MANAGER:  
     1 Обновлять информацию о пользователях (имя, email)  
     2. Создавать задачи  
     3. Обновлять данные задач (title, description, status, priority, executorID, deadline)  
     4. Получать список пользователей  
     5. Получать по id пользователя  
     6. Получать список всех задач  
     7. Удалять задачу  
-    #### USER может:  
     1 Просматривать только свои задачи  
     2. Обновлять статус своей задачи  
    3. Получать список пользователей  
    4. Получать пользователя по id  
    5. Получать список своих задач  
    6. Обновлять данные своей задачи (status)  
  
### Задачи:  
- У каждой задачи есть один создатель и один исполнитель  
- У пользователя может быть несколько созданных и выполняемых задач  
- Полная реляционная связь реализована через JPA @OneToMany и @ManyToOne  
- Пагинация списков задач  
- Фильтрация реализована через Specification (по status, priority, executorID, deadline)


### AUTH API
| Метод | URL                 | Описание                    |  
|-------|---------------------|-----------------------------|  
| POST  | /api/auth/login     | Аутентификация пользователя |  
| POST  | /api/auth/refresh   | Обновить временный токен    |  

#### Запрос на аутентификацию пользователя

```
  {
        "username": "Yura Sharko",
        "password": "12345"
  }
```
#### Запрос на обновление токена
```
  {
    "refreshToken": "your token"
  }
```
   

### USER API  
| Метод | URL                 | Роль                    | Описание                           |  
|-------|---------------------|-------------------------|------------------------------------|  
| POST  | /api/users          | ADMIN                   | Создать пользователя               |  
| PUT   | /api/users/{id}     | ADMIN, MANAGER          | Обновить данные пользователя       |  
| GET   | /api/users          | Все роли                | Получить всех пользователей        |  
| GET   | /api/users/{id}     | Все роли                | Получить пользователя по ID        |  
| DELETE| /api/users/{id}     | ADMIN                   | Удалить пользователя               |  
| GET   | /api/users/username | Все роли                | Получить пользователя по username  |  
| GET   | /api/users/email    | Все роли                | Получить пользователя по email     |  

### Запрос на создание пользователя:  
```
{  
        "username": "manager2",  
        "email": "manager2@gmail.com",  
        "role": "MANAGER",  
        "password" : "123456",  
        "passwordConfirm" : "123456"  
}  
```
### Запрос на обновление пользователя:  
```
{  
        "username": "AZAAA",  
        "email": "rabaza@gmail.com"  
}
```

## TASK API  
| Метод  | URL                           | Роль                    | Описание                             |
|--------|-------------------------------|-------------------------|--------------------------------------|
| POST   | /api/tasks                    | ADMIN, MANAGER          | Создать задачу                       |
| PATCH  | /api/tasks/{id}               | Все роли                | Обновить задачу                      |
| GET    | /api/tasks                    | Все роли                | Получить все задачи                  |  
| DELETE | /api/tasks/{id}               | ADMIN, MANAGER          | Удаление задачи                      |  
| GET    | /api/tasks/{id}               | Все роли                | Поиск задачи по id                   |   
  
### Запрос на создание задачи:  
```
{  
    "title": "test6",  
    "description": "text6",  
    "deadline": "2026-03-27T23:59:00"  
}
```

### Запрос на обновление данных задачи:  
```
{  
        "title": "test2000",  
        "description": "text2000",  
        "priority": "LOW",  
        "status": "IN_PROGRESS",  
        "executorID": 2  
}
```
