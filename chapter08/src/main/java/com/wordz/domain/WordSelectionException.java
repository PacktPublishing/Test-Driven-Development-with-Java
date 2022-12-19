package com.wordz.domain;

public class WordSelectionException extends RuntimeException {
    public WordSelectionException(String reason, Throwable t) {
        super(reason, t);
    }
}
