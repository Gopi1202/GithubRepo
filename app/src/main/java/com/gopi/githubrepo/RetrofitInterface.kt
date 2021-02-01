package com.gopi.githubrepo

import com.gopi.githubrepo.model.GithubModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("repositories")
    fun getRepoList(@Query("since") page: Int) : Call<List<GithubModel>?>?
}