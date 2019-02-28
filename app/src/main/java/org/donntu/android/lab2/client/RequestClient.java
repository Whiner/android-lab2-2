package org.donntu.android.lab2.client;

import org.donntu.android.lab2.dto.NextWordResponse;
import org.donntu.android.lab2.dto.WordWithTranslation;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RequestClient {
    @GET("/next-word")
    NextWordResponse getNextWord(@Query("answerVersionsCount") int answerVersionsCount);

    @POST("/inc-right-answer")
    void incRightAnswer(@Query("wordId") Long wordId);

    @PUT("/add")
    void addWord(@Query("word") WordWithTranslation word);
}
