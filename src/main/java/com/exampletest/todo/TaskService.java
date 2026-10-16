package com.exampletest.todo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private static final String TASK_NOT_FOUND_FOR_GIVEN_ID = "Cannot find task with given id";
    private static final String TASK_DESC_REQUIRED = "Task's description is required";

    private final TaskRepository taskRepository;


    @Transactional
    public TaskUpdateRequestDto updateTask(Long taskId, TaskUpdateRequestDto taskUpdateRequest) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException(TASK_NOT_FOUND_FOR_GIVEN_ID);
        }

        if (taskUpdateRequest == null || taskUpdateRequest.description() == null) {
            throw new IllegalArgumentException(TASK_DESC_REQUIRED);
        }

        Task taskToUpdate = taskOptional.get();
        taskToUpdate.setDescription(taskUpdateRequest.description());
        taskToUpdate.setPriority(taskUpdateRequest.priority());
        taskRepository.save(taskToUpdate);
        return taskUpdateRequest;
    }
}
