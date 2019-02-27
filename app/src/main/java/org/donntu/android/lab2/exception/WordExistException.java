package org.donntu.android.lab2.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WordExistException extends Exception {
    public WordExistException(String message) {
        super(message);
    }

}
