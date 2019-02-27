package org.donntu.android.lab2.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotEnoughWordsException extends Exception {
    public NotEnoughWordsException(String message) {
        super(message);
    }

}
