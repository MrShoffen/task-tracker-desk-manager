package org.mrshoffen.tasktracker.task.manager.exception;

public class TaskAlreadyExistsException extends RuntimeException{
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
