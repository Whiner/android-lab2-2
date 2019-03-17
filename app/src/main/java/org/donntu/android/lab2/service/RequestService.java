package org.donntu.android.lab2.service;


import android.content.Intent;

import org.donntu.android.lab2.client.RequestClient;
import org.donntu.android.lab2.dto.NextWordResponse;
import org.donntu.android.lab2.exception.NotEnoughWordsException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v4.content.ContextCompat.startActivity;

public class RequestService {
    static final String BASE_URL = "http://192.168.1.104:8080/";
    private RequestClient requestClient;

    public RequestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        requestClient = retrofit.create(RequestClient.class);
    }

    public NextWordResponse getNextWord(int answerVersionsCount) throws Exception {
        AtomicReference<Object> nextWordResponse = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                Response<NextWordResponse> response = requestClient.getNextWord(answerVersionsCount).execute();
                if(response.isSuccessful()) {
                    nextWordResponse.set(response.body());
                } else {
                    nextWordResponse.set(new NotEnoughWordsException("Недостаточно слов в базе. Добавьте еще или очистите архив"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        Object o = nextWordResponse.get();
        if(o instanceof NotEnoughWordsException) {
            throw (NotEnoughWordsException) o;
        }
        return (NextWordResponse) nextWordResponse.get();
    }


    public void incRightAnswer(Long wordId) {
        Call<ResponseBody> call = requestClient.incRightAnswer(wordId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public Integer getAvailableWordsCount() throws InterruptedException {
        AtomicInteger atomicCount = new AtomicInteger();
        Thread thread = new Thread(() -> {
            try {
                Response<Integer> execute = requestClient.getAvailableWordsCount().execute();
                atomicCount.set(execute.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        return atomicCount.get();
    }
}