package com.carlos.rxretrofit.api;

import com.carlos.rxretrofit.model.GitHubRepo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebServiceApi {

    //sin Rx
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>> getReposForUser(@Path("user") String user);

    //con Rx
    @GET("/users/{user}/repos")
    Single<List<GitHubRepo>> getReposForUserRx(@Path("user") String user);



}
