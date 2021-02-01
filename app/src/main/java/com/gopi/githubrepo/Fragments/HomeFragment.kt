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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.gopi.githubrepo.R
import com.gopi.githubrepo.adapter.RepoAdapter
import com.gopi.githubrepo.RepoClickListener
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

class HomeFragment : Fragment(), RepoClickListener {

    private val tagName = HomeFragment::class.simpleName
    private val BASE_URL = "https://api.github.com/"
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var repoList: ArrayList<GithubModel> = ArrayList()
    private lateinit var repoAdapter: RepoAdapter
    private var loading: Boolean = false
    private var lastItemId: Int = 0
    private var repoListAsString: String = ""
    private var position: Int = 0

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

        repoAdapter = RepoAdapter(context!!, repoList, this)
        rvRepoList.adapter = repoAdapter

        rvRepoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.v(tagName, "dx: $dx, dy: $dy")
                if (dy > 0) {
                    var lastCompletelyVisibleItemPosition = 0
                    lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()

                    if (!loading && lastCompletelyVisibleItemPosition == (repoList.size - 1)) {
                        loading = true
                        Log.v(tagName, "lastCompletelyVisibleItemPosition: $lastCompletelyVisibleItemPosition")
                        lastItemId = repoList[lastCompletelyVisibleItemPosition].id
                        loadMoreData(lastItemId)
                    }
                }
            }
        })

        if (repoListAsString.isNotEmpty()) {
            val gson = Gson()
            val typeToken = object : TypeToken<ArrayList<GithubModel>>() {}.type
            val list = gson.fromJson<ArrayList<GithubModel>>(
                repoListAsString,
                typeToken
            )
            if (list.isNotEmpty()) {
                repoList.clear()
                repoList.addAll(list)
                repoAdapter.notifyDataSetChanged()
                rvRepoList.scrollToPosition(position)
            }
        } else {
            getRepoList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null) {
            repoListAsString = arguments!!.getString("REPO_LIST") ?: ""
            position = arguments!!.getInt("POSITION")

            Log.v(tagName, "repoListAsString: $repoListAsString")
            Log.v(tagName, "position: $position")
        }
    }

    private fun loadMoreData(lastItemId: Int) {
        Log.v(tagName, "lastItemId: $lastItemId")
        getRepoListAction(lastItemId)
    }

    private fun getRepoList() {
        if (isInternetAvailable(context!!)) {
            repoList.clear()
            getRepoListAction(0)
        } else {
            Toast.makeText(context, "Check your internet connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRepoListAction(page: Int) {
        //show pagination progress based on API call
        if (!loading) {
            clProgress.visibility = View.VISIBLE
        } else {
            clProgressTwo.visibility = View.VISIBLE
        }

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
        val call: Call<List<GithubModel>?>? = api.getRepoList(lastItemId)

        call?.enqueue(object : Callback<List<GithubModel>?> {
            override fun onResponse(
                call: Call<List<GithubModel>?>,
                response: Response<List<GithubModel>?>
            ) {
                Log.v(tagName, "Response: " + response.body().toString())
                clProgress.visibility = View.GONE
                clProgressTwo.visibility = View.GONE
                loading = false

                if (response.body() != null) {
                    try {
                        if (response.body()!!.isEmpty()) {
                            loading = true
                        } else {
                            repoList.addAll(response.body()!!)

                            Log.v(tagName, "repoList size: " + repoList.size)
                        }
                    } catch (e: Exception) {
                        Log.v(tagName, "Exception: " + e.localizedMessage)
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GithubModel>?>, t: Throwable) {
                Log.v(tagName, "onFailure: " + t.message)
                clProgress.visibility = View.GONE
                clProgressTwo.visibility = View.GONE

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

    override fun repoClicked(position: Int) {
        Log.v(tagName, "clickedPos: $position")

        val gson = Gson()

        val bundle = bundleOf(
            "POSITION" to position,
            "NAME" to repoList[position].name,
            "FULL_NAME" to repoList[position].fullName,
            "LOGIN" to repoList[position].owner.login,
            "AVATAR" to repoList[position].owner.avatarUrl,
            "DESC" to repoList[position].description,
            "REPO_LIST" to gson.toJson(repoList)
        )

        val navController = findNavController(activity!!, R.id.nav_host_fragment)
        if (navController.currentDestination?.id == R.id.homeFragment) {
            navController.navigate(R.id.detailFragment, bundle)
        }
    }
}