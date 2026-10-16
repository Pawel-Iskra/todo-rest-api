# todo-rest-api
Todo REST API endpoint as a solution to a coding assessment.

#### Requirements:
- Spring Boot version 3.5.0
- spring-boot-starter-web, 
- spring-boot-starter-data-jpa, 
- JDK 21, 
- Hibernate version 6.4.4.Final,
- Task class as hibernate entity.

#### Database Schema:   
```sql
CREATE TABLE task (  
  id bigint NOT NULL,  
  description varchar(200) NOT NULL,  
  priority bigint,  
  PRIMARY KEY (id));
```
#### API Specification:      
##### Update task
- Endpoint: PUT  /tasks/{id} with requestBody
  ```json
      { 
       "description" : "task's description",
       "priority" : 5  
      }
  ```
##### Responses:
- 200 OK -> task successfully updated return response body same as request body
- 400 Bad Requestt -> task description is null or does not exist, response body:
  ```json
     {
       "message" : "Task's description is required",
       "status" : 400
     }
  ```
- 404 Not Found -> no task exist for given id, response body:
  ```json
     {
       "message" : "Cannot find task with given id",
       "status" : 404
     }
  ```
- Error precedence: first check id (404), if exist then validate request body (400)     
