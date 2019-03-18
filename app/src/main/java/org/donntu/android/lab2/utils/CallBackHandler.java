package org.donntu.android.lab2.utils;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBackHandler<T> implements Callback<T> {
    private static Consumer<String> exceptionHandler;

    public static void setExceptionHandler(Consumer<String> exceptionHandler) {
        CallBackHandler.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        exceptionHandler.accept(t.getMessage());
    }
}
