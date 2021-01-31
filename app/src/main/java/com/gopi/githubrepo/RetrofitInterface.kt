package com.gopi.githubrepo

import com.gopi.githubrepo.model.GithubModel
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface {

    @GET("repositories")
    fun getRepoList() : Call<List<GithubModel>?>?
}