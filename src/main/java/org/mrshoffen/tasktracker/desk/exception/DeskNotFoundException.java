package org.mrshoffen.tasktracker.desk.exception;

public class DeskNotFoundException extends RuntimeException{
    public DeskNotFoundException(String message) {
        super(message);
    }
}
