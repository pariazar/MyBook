package com.example.ketabeman21.Network;

import org.apache.http.entity.mime.content.StringBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface RequestInterface {

    @FormUrlEncoded
    @POST("/login.php")
    Call<String>  getUserLogin(
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("/register.php")
    Call<String>  getUserRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("pic") String pic,
            @Field("password") String password
    );

    @GET("/")
    Call<JSONResponse> getJSONBook(@Query("action") String action);
    
    @GET("/")
    Call<JSONResponse> getSubCat(@Query("action") String action,@Query("sub") String Catid);

    @GET("/newestbook.php")
    Call<JSONResponse> getJSONNewestBook();


    @GET("/jsonofcode.php")
    Call<JSONResponse> getJSONCode();



    @GET("uploads/books/{input}")
    @Streaming
    Call<ResponseBody> downloadFile(@Path("input") String input, @Query("format") String format);




    @FormUrlEncoded
    @POST("/jsonsonsbooks.php")
    Call<JSONResponse> post(
            @Field("cat") String cat

    );

    @FormUrlEncoded
    @POST("/Beta/xx/api.php")
    Call<JSONResponse> getAllCode(
            @Field("action") String action

    );

    @FormUrlEncoded
    @POST("/Beta/xx/api.php")
    Call<JSONResponse> getFollowersBook(
            @Field("email") String email,
            @Field("action") String action

    );
    @FormUrlEncoded
    @POST("/comments.php")
    Call<JSONResponse> ShowAllComments(
            @Field("action") int action,
            @Field("bookId") String bookId
          /*  @Field("bookId") String bookId,
            @Field("userEmail") String userEmail,
            @Field("message") String message*/

    );
    @FormUrlEncoded
    @POST("/insertBook.php")
    Call<StringBody> SendBooks(
            @Field("user") String user,
            @Field("name") String name,
            @Field("writer") String writer,
            @Field("cat") String cat,
            @Field("edition") String edition,
            @Field("link") String link,
            @Field("page") String page,
            @Field("pic") String pic,
            @Field("des") String des,
            @Field("cost") String cost


    );
    @FormUrlEncoded
    @POST("/comments.php")
    Call<JSONResponse> ShowAllUsers(
            @Field("action") int action
    );

    @FormUrlEncoded
    @POST("/comments.php")
    Call<JSONResponse> ShowUniUsers(
            @Field("action") int action,
            @Field("userEmail") String user
    );
    @FormUrlEncoded
    @POST("/comments.php")
    Call<StringBody> FollowUsers(
            @Field("action") int action,
            @Field("userEmail") String user,
            @Field("targetForFollow") String target
    );
    @FormUrlEncoded
    @POST("/comments.php")
    Call<JSONResponse> getNewestUsers(
            @Field("action") int action
    );

    @GET("/en/api.php")
    Call<JSONResponse> getJSONCourse();

    @FormUrlEncoded
    @POST("/en/onlineCourse.php")
    Call<JSONResponse> getVideoFiles(
            @Field("fileName") String fileName
    );

    @FormUrlEncoded
    @POST("/codeAPI.php")
    Call<JSONResponse> getCodeFiles(
            @Field("dirname") String dirname
    );
}
