package com.gopi.githubrepo.model

import com.google.gson.annotations.SerializedName

data class GithubModel(

    @field:SerializedName("tags_url")
    val tagsUrl: String,

    @field:SerializedName("private")
    val jsonMemberPrivate: Boolean,

    @field:SerializedName("contributors_url")
    val contributorsUrl: String,

    @field:SerializedName("notifications_url")
    val notificationsUrl: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("subscription_url")
    val subscriptionUrl: String,

    @field:SerializedName("keys_url")
    val keysUrl: String,

    @field:SerializedName("branches_url")
    val branchesUrl: String,

    @field:SerializedName("deployments_url")
    val deploymentsUrl: String,

    @field:SerializedName("issue_comment_url")
    val issueCommentUrl: String,

    @field:SerializedName("labels_url")
    val labelsUrl: String,

    @field:SerializedName("subscribers_url")
    val subscribersUrl: String,

    @field:SerializedName("releases_url")
    val releasesUrl: String,

    @field:SerializedName("comments_url")
    val commentsUrl: String,

    @field:SerializedName("stargazers_url")
    val stargazersUrl: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("owner")
    val owner: Owner,

    @field:SerializedName("archive_url")
    val archiveUrl: String,

    @field:SerializedName("commits_url")
    val commitsUrl: String,

    @field:SerializedName("git_refs_url")
    val gitRefsUrl: String,

    @field:SerializedName("forks_url")
    val forksUrl: String,

    @field:SerializedName("compare_url")
    val compareUrl: String,

    @field:SerializedName("statuses_url")
    val statusesUrl: String,

    @field:SerializedName("git_commits_url")
    val gitCommitsUrl: String,

    @field:SerializedName("blobs_url")
    val blobsUrl: String,

    @field:SerializedName("git_tags_url")
    val gitTagsUrl: String,

    @field:SerializedName("merges_url")
    val mergesUrl: String,

    @field:SerializedName("downloads_url")
    val downloadsUrl: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("contents_url")
    val contentsUrl: String,

    @field:SerializedName("milestones_url")
    val milestonesUrl: String,

    @field:SerializedName("teams_url")
    val teamsUrl: String,

    @field:SerializedName("fork")
    val fork: Boolean,

    @field:SerializedName("issues_url")
    val issuesUrl: String,

    @field:SerializedName("full_name")
    val fullName: String,

    @field:SerializedName("events_url")
    val eventsUrl: String,

    @field:SerializedName("issue_events_url")
    val issueEventsUrl: String,

    @field:SerializedName("languages_url")
    val languagesUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("collaborators_url")
    val collaboratorsUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("pulls_url")
    val pullsUrl: String,

    @field:SerializedName("hooks_url")
    val hooksUrl: String,

    @field:SerializedName("assignees_url")
    val assigneesUrl: String,

    @field:SerializedName("trees_url")
    val treesUrl: String,

    @field:SerializedName("node_id")
    val nodeId: String
)