package com.exampletest.todo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
class TaskControllerTest {


    private static final String PUT_PATH = "/tasks/%d";
    private static final Long TASK_ID_ONE = 1L;
    private static final Long TASK_PRIORITY_TWO = 2L;
    private static final String REQUEST_TASK_DESCRIPTION_JSON = "\"task description\"";
    private static final String REQUEST_TASK_DESCRIPTION_STRING = REQUEST_TASK_DESCRIPTION_JSON.replace("\"", "");
    private static final String TASK_NOT_FOUND_FOR_GIVEN_ID = "Cannot find task with given id";
    private static final String TASK_DESC_REQUIRED = "Task's description is required";
    private static final String JSON_REQUEST = """
            {
                "description" : %s,
                "priority" : %d
            }""";


    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TaskService taskService;


    @Test
    public void shouldReturnStatusOkForSuccessfullyUpdatedTask() throws Exception {
        // given
        TaskUpdateRequestDto taskUpdateRequestDto = new TaskUpdateRequestDto(REQUEST_TASK_DESCRIPTION_STRING, TASK_PRIORITY_TWO);
        String validJsonStringRequest = String.format(JSON_REQUEST, REQUEST_TASK_DESCRIPTION_JSON, TASK_PRIORITY_TWO);
        String putPath = String.format(PUT_PATH, TASK_ID_ONE);
        Mockito.when(taskService.updateTask(TASK_ID_ONE, taskUpdateRequestDto))
                .thenReturn(taskUpdateRequestDto);

        // when + then
        mockMvc.perform(put(putPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonStringRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(REQUEST_TASK_DESCRIPTION_STRING))
                .andExpect(jsonPath("$.priority").value(TASK_PRIORITY_TWO));

        verify(taskService).updateTask(TASK_ID_ONE, taskUpdateRequestDto);
    }


    @Test
    public void shouldReturnStatusBadRequestWhenServiceThrowsIllegalArgumentException() throws Exception {
        // given
        String notValidJsonStringRequest = String.format(JSON_REQUEST, null, TASK_PRIORITY_TWO);
        String putPath = String.format(PUT_PATH, TASK_ID_ONE);
        Mockito.when(taskService.updateTask(any(Long.class), any(TaskUpdateRequestDto.class)))
                .thenThrow(new IllegalArgumentException(TASK_DESC_REQUIRED));

        // when + then
        mockMvc.perform(put(putPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notValidJsonStringRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(TASK_DESC_REQUIRED))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void shouldReturnStatusNotFoundWhenTaskDoesNotExist() throws Exception {
        // given
        String validJsonStringRequest = String.format(JSON_REQUEST, REQUEST_TASK_DESCRIPTION_JSON, TASK_PRIORITY_TWO);
        String putPath = String.format(PUT_PATH, TASK_ID_ONE);
        Mockito.when(taskService.updateTask(any(Long.class), any(TaskUpdateRequestDto.class)))
                .thenThrow(new TaskNotFoundException(TASK_NOT_FOUND_FOR_GIVEN_ID));

        // when + then
        mockMvc.perform(put(putPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonStringRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(TASK_NOT_FOUND_FOR_GIVEN_ID))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

}