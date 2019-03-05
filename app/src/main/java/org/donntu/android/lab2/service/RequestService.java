package org.donntu.android.lab2.service;


import android.util.Log;

import org.donntu.android.lab2.client.RequestClient;
import org.donntu.android.lab2.dto.NextWordResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestService {
    static final String BASE_URL = "http://192.168.0.102:8080/";
    private RequestClient requestClient;

    public RequestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        requestClient = retrofit.create(RequestClient.class);
    }

    public NextWordResponse getNextWord(int answerVersionsCount) throws InterruptedException {
        AtomicReference<NextWordResponse> nextWordResponse = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                nextWordResponse.set(requestClient.getNextWord(answerVersionsCount).execute().body());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        return nextWordResponse.get();
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

}