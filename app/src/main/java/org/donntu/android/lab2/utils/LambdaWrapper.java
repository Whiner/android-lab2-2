package org.donntu.android.lab2.utils;

import android.util.Log;

import java.util.function.Consumer;

public class LambdaWrapper {

    private static Consumer<String> exceptionHandler;

    public static Consumer<Object> lambdaWrapper(Consumer<Object> consumer) {
        return object -> {
            try {
                consumer.accept(object);
            } catch (Exception e) {
                if(exceptionHandler != null) {
                    exceptionHandler.accept(e.getMessage());
                }
                Log.e("lambda-wrapper", e.getMessage());
            }
        };
    }

    public static Consumer<Void> lambdaWrapper(Runnable runnable) {
        return (v) -> {
            try {
                runnable.run();
            } catch (Exception e) {
                if(exceptionHandler != null) {
                    exceptionHandler.accept(e.getMessage());
                }
                Log.e("lambda-wrapper", e.getMessage());
            }
        };
    }

    public static void setExceptionHandler(Consumer<String> exceptionHandler) {
        LambdaWrapper.exceptionHandler = exceptionHandler;
    }
}
