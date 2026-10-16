package com.exampletest.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskNotFoundException extends RuntimeException {

    private final String exceptionMessage;
}
