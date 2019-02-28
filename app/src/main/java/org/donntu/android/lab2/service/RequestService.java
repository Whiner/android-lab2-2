package org.donntu.android.lab2.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.donntu.android.lab2.client.RequestClient;
import org.donntu.android.lab2.dto.NextWordResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestService {
    static final String BASE_URL = "http://192.168.0.107/api/";
    private RequestClient requestClient;

    public RequestService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        requestClient = retrofit.create(RequestClient.class);
    }

    public NextWordResponse getNextWord(int answerVersionsCount){
        return requestClient.getNextWord(answerVersionsCount);
    }


    public void incRightAnswer(Long wordId) {
        requestClient.incRightAnswer(wordId);
    }
}