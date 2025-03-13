package org.hiss.exceptions;

public class SamePasswordException extends RuntimeException {

    public SamePasswordException() {
        super("Cannot change to old password.");
    }
}
