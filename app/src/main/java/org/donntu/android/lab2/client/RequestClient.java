package org.donntu.android.lab2.client;

import org.donntu.android.lab2.dto.NextWordResponse;
import org.donntu.android.lab2.dto.WordWithTranslation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RequestClient {
    @GET("/next-word")
    Call<NextWordResponse> getNextWord(@Query("answerVersionsCount") int answerVersionsCount);

    @POST("/inc-right-answer")
    Call<ResponseBody> incRightAnswer(@Query("wordId") Long wordId);

    @PUT("/add")
    Call<ResponseBody> addWord(@Query("word") WordWithTranslation word);
}
