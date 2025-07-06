package it.giovanni.hub.data.model.comfyui

import com.google.gson.annotations.SerializedName

data class HistoryItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("run_time")
    val runTime: Long,

    @SerializedName("status")
    val status: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("output")
    val output: List<Output>,

    @SerializedName("workflow_id")
    val workflowId: String,

    @SerializedName("api_key_id")
    val apiKeyId: String,

    @SerializedName("device")
    val device: String
)

data class Output(
    @SerializedName("filename")
    val filename: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String
)