package com.exampletest.todo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TaskService {

    private static final String TASK_NOT_FOUND_FOR_GIVEN_ID = "Cannot find task with given id";
    private static final String TASK_DESC_REQUIRED = "Task's description is required";

    private final TaskRepository taskRepository;


    @Transactional
    public TaskUpdateRequestDto updateTask(Long taskId, TaskUpdateRequestDto taskUpdateRequest) {
        Task taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_FOR_GIVEN_ID));

        if (taskUpdateRequest == null || taskUpdateRequest.description() == null) {
            throw new IllegalArgumentException(TASK_DESC_REQUIRED);
        }

        taskToUpdate.setDescription(taskUpdateRequest.description());
        taskToUpdate.setPriority(taskUpdateRequest.priority());
        taskRepository.save(taskToUpdate);
        // not necessary - hibernate detects changes to managed entity and persists them at transaction commit
        return taskUpdateRequest;
    }
}
