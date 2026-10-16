package com.exampletest.todo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PutMapping("/{id}")
    public ResponseEntity<TaskUpdateRequestDto> updateTask(
            @PathVariable("id") Long taskId,
            @RequestBody TaskUpdateRequestDto requestDto) {

        TaskUpdateRequestDto taskUpdateResult = taskService.updateTask(taskId, requestDto);
        return new ResponseEntity<>(taskUpdateResult, HttpStatus.OK);
    }
}
