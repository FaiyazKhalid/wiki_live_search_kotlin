package com.example.wikisearch.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class ServiceResponse(
    val data: WikiModelRoot? = null,
    val code: Int = 0,
    val message: String,
    val isSuccess: Boolean = true
)

data class Continue(
    val gpsoffset: Int,
    @SerializedName("continue")
    val continues: String? = null
)

data class Page(
    val pageid: Int,
    val ns: Int,
    val title: String?,
    val index: Int,
    val thumbnail: Thumbnail?,
    val terms: Terms?
)

class Query(
    val redirects: ArrayList<Redirect>?,
    val pages: ArrayList<Page>?
)

class Redirect(
    val index: Int,
    val from: String?,
    val to: String? = null
)

class WikiModelRoot(
    val batchcomplete: Boolean?,
    @SerializedName("continue")
    val mycontinue: Continue?,
    val query: Query?
)

class Terms(
    val description: List<String>?,
)

class Thumbnail(
    val source: String?,
    val width: Int,
    val height: Int,
)