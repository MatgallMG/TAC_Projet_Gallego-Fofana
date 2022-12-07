package com.example.tac_projet_gallego_fofana.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API_Client {

    public static String BASE_URL ="https://api.themoviedb.org/3/";

    private static Retrofit retrofit;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
