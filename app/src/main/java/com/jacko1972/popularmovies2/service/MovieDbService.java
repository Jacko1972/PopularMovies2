package com.jacko1972.popularmovies2.service;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

public class MovieDbService {

    private static final String MOVIE_DB_API_URL = "https://api.themoviedb.org/3/movie/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            //Uncomment three lines and the import above to enable logging for Retrofit API calls
            //HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            //httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //okHttpClient.addInterceptor(httpLoggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_DB_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build();
        }
        return retrofit;
    }

}
