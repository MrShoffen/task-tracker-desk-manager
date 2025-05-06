package org.mrshoffen.tasktracker.desk.exception;

public class DeskAlreadyExistsException extends RuntimeException{
    public DeskAlreadyExistsException(String message) {
        super(message);
    }
}
