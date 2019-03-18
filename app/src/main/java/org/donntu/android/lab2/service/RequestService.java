package org.donntu.android.lab2.service;


import org.donntu.android.lab2.client.RequestClient;
import org.donntu.android.lab2.dto.FullWordInfo;
import org.donntu.android.lab2.dto.NextWordResponse;
import org.donntu.android.lab2.dto.WordWithTranslation;
import org.donntu.android.lab2.exception.NotEnoughWordsException;
import org.donntu.android.lab2.utils.CallBackHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.donntu.android.lab2.utils.LambdaWrapper.lambdaWrapper;

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

    public NextWordResponse getNextWord(int answerVersionsCount) throws InterruptedException {
        AtomicReference<NextWordResponse> nextWordResponse = new AtomicReference<>();
        Thread thread = new Thread((Runnable) lambdaWrapper(
                () -> {
                    try {
                        Response<NextWordResponse> response
                                = requestClient.getNextWord(answerVersionsCount).execute();
                        if (response.isSuccessful()) {
                            nextWordResponse.set(response.body());
                        } else {
                            throw new NotEnoughWordsException("Недостаточно слов в базе. Добавьте еще или очистите архив");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
        ));
        thread.start();
        thread.join();
        return nextWordResponse.get();
    }


    public void incRightAnswer(Long wordId) {
        Call<ResponseBody> call = requestClient.incRightAnswer(wordId);
        call.enqueue(new CallBackHandler<>());
    }

    public Integer getAvailableWordsCount() throws InterruptedException {
        AtomicInteger atomicCount = new AtomicInteger();
        Thread thread = new Thread(
                (Runnable) lambdaWrapper(() -> {
                    try {
                        Response<Integer> execute = requestClient.getAvailableWordsCount().execute();
                        atomicCount.set(execute.body());
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }));
        thread.start();
        thread.join();
        return atomicCount.get();
    }

    public List<FullWordInfo> getAll() throws InterruptedException {
        AtomicReference<List<FullWordInfo>> words = new AtomicReference<>();
        Thread thread = new Thread((Runnable) lambdaWrapper(() -> {
                    try {
                        Response<List<FullWordInfo>> execute = requestClient.getAllWords().execute();
                        words.set(execute.body());
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
        ));
        thread.start();
        thread.join();
        return words.get();
    }

    public void add(List<WordWithTranslation> words) {
        Call<ResponseBody> call = requestClient.addWords(words);
        call.enqueue(new CallBackHandler<>());
    }
}