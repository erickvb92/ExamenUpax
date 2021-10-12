package com.upax.aplicationupax.model
import com.google.gson.annotations.SerializedName

data class Response_moviedb (

	@SerializedName("average_rating") val average_rating : String,
	@SerializedName("backdrop_path") val backdrop_path : String,
	@SerializedName("comments") val comments : Comments,
	@SerializedName("created_by") val created_by : Created_by,
	@SerializedName("description") val description : String,
	@SerializedName("id") val id : String,
	@SerializedName("iso_3166_1") val iso_3166_1 : String,
	@SerializedName("iso_639_1") val iso_639_1 : String,
	@SerializedName("name") val name : String,
	@SerializedName("object_ids") val object_ids : Object_ids,
	@SerializedName("page") val page : String,
	@SerializedName("poster_path") val poster_path : String,
	@SerializedName("public") val public : Boolean,
	@SerializedName("results") val results : List<Results>,
	@SerializedName("revenue") val revenue : String,
	@SerializedName("runtime") val runtime : String,
	@SerializedName("sort_by") val sort_by : String,
	@SerializedName("total_pages") val total_pages : String,
	@SerializedName("total_results") val total_results : String
)