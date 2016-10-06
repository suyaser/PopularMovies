package com.training.yasser.popularmovies.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.training.yasser.popularmovies.models.ActorResponse;
import com.training.yasser.popularmovies.models.MovieResponse;
import com.training.yasser.popularmovies.models.ReviewResponse;
import com.training.yasser.popularmovies.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yasser on 06/10/2016.
 */

public class ApiClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    public static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private static MovieDBApi sMovieDBApi;


    public static MovieDBApi getMovieDBApi(){
        if(sMovieDBApi == null){
            sMovieDBApi = retrofit.create(MovieDBApi.class);
        }
        return  sMovieDBApi;
    }

    public interface MovieDBApi {

        @GET("{sort}?api_key=336043898c0bea0b096943d0349c541c")
        Call<MovieResponse> getMovieList(
                @Path("sort") String sort,
                @Query("page") int page);

        @GET("{id}/reviews?api_key=336043898c0bea0b096943d0349c541c")
        Call<ReviewResponse> getReviewList(
                @Path("id") int id);

        @GET("{id}/credits?api_key=336043898c0bea0b096943d0349c541c")
        Call<ActorResponse> getActorList(
                @Path("id") int id);

        @GET("{id}/trailers?api_key=336043898c0bea0b096943d0349c541c")
        Call<TrailerResponse> getTrailerList(
                @Path("id") int id);

    }
}
