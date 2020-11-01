package com.example.ketabeman21.Network;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;


public interface RetrofitInterface {

    @POST("/listOfAllBook")
    Call<JSONResponse> getJSONBook(
            @Query("userID") String userID,
            @Query("pass") String pass
    );
}
