package com.gopi.githubrepo.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gopi.githubrepo.R
import com.gopi.githubrepo.adapter.CommentAdapter
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {

    private var position: Int = -1
    private var name: String = ""
    private var fullName: String = ""
    private var loginUser: String = ""
    private var avatarUrl: String = ""
    private var desc: String = ""
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var commentList: ArrayList<String> = ArrayList()
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var repoListAsString: String
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            position = arguments!!.getInt("POSITION")
            name = arguments!!.getString("NAME").toString()
            fullName = arguments!!.getString("FULL_NAME").toString()
            loginUser = arguments!!.getString("LOGIN").toString()
            avatarUrl = arguments!!.getString("AVATAR").toString()
            desc = arguments!!.getString("DESC").toString()

            repoListAsString = arguments!!.getString("REPO_LIST") ?: ""

            Log.v("tag", "position: $position")
            Log.v("tag", "name: $name")
            Log.v("tag", "fullName: $fullName")
            Log.v("tag", "loginUser: $loginUser")
            Log.v("tag", "avatarUrl: $avatarUrl")
            Log.v("tag", "desc: $desc")
        }

        //handling onBackPressed event
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackAction()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun onBackAction() {
        val bundle = bundleOf("REPO_LIST" to repoListAsString,
            "POSITION" to position)
        if (navController.currentDestination?.id == R.id.detailFragment) {
            navController.navigate(R.id.action_detailFragment_to_homeFragment, bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        //set linearLayoutManager & adapter
        linearLayoutManager = LinearLayoutManager(context)
        rvCommentList.layoutManager = linearLayoutManager

        commentAdapter = CommentAdapter(context!!, commentList)
        rvCommentList.adapter = commentAdapter

        //load data
        Glide.with(this)
            .load(avatarUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            /*.placeholder(R.drawable.ic_movie_black_18dp)
            .error(R.drawable.ic_movie_black_18dp)*/
            .into(ivProfileImage)

        tvUserName.text = loginUser

        tvDesc.text = desc

        //tvRepoName.text = fullName

        val sharedPreferences = context!!.getSharedPreferences("COMMENTS", Context.MODE_PRIVATE)

        if(sharedPreferences.getStringSet(name, null) != null) {
            //load data
            val getList: Set<String> = sharedPreferences.getStringSet(name, null) as Set<String>
            Log.v("tag", "getList size: " + getList.size)
            commentList.addAll(getList)
            commentAdapter.notifyDataSetChanged()
        }

        ivSend.setOnClickListener {
            val comment = etEditComment.text.toString().trim()
            Log.v("tag", "comment: $comment")

            if(comment.isEmpty()){
                Toast.makeText(context, "Please enter comments", Toast.LENGTH_SHORT).show()
            } else {
                commentList.add(comment)
                commentAdapter.notifyDataSetChanged()

                val editor = sharedPreferences.edit()

                //Set the values
                val set: MutableSet<String> = HashSet()
                set.addAll(commentList)
                editor.putStringSet(name, set)
                editor.commit()
                editor.apply()

                etEditComment.setText("")
            }
        }
    }
}
