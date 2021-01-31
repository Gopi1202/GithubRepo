package com.gopi.githubrepo.Fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.gopi.githubrepo.R
import com.gopi.githubrepo.RepoAdapter
import com.gopi.githubrepo.RetrofitInterface
import com.gopi.githubrepo.model.GithubModel
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private val tagName = HomeFragment::class.simpleName
    private val BASE_URL = "https://api.github.com/"
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var repoList: ArrayList<GithubModel> = ArrayList()
    private lateinit var repoAdapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        tvEmpty.visibility = View.GONE
        rvRepoList.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(context)
        rvRepoList.layoutManager = linearLayoutManager

        repoAdapter = RepoAdapter(context!!, repoList)
        rvRepoList.adapter = repoAdapter

        getRepoList()
    }

    private fun getRepoList() {
        if (isInternetAvailable(context!!)) {
            getRepoListAction()
        } else {
            Toast.makeText(context, "Check your internet connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRepoListAction() {
        clProgress.visibility = View.VISIBLE

        //The gson builder
        val gson = GsonBuilder()
            .setLenient()
            .create()

        //to handle timeout
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        //creating retrofit object
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        //creating our api
        val api: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)

        //creating a api call to get audio list
        val call: Call<List<GithubModel>?>? = api.getRepoList()

        call?.enqueue(object : Callback<List<GithubModel>?> {
            override fun onResponse(
                call: Call<List<GithubModel>?>,
                response: Response<List<GithubModel>?>
            ) {
                Log.v(tagName, "Response: " + response.body().toString())
                clProgress.visibility = View.GONE

                repoList.clear()
                repoList.addAll(response.body()!!)

                Log.v(tagName, "repoList size: " + repoList.size)


            }

            override fun onFailure(call: Call<List<GithubModel>?>, t: Throwable) {
                Log.v(tagName, "onFailure: " + t.message)
                clProgress.visibility = View.GONE
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    //to check internet connection
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }
}