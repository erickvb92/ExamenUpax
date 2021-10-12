package com.upax.aplicationupax.model
import com.google.gson.annotations.SerializedName
data class Created_by (

	@SerializedName("gravatar_hash") val gravatar_hash : String,
	@SerializedName("id") val id : String,
	@SerializedName("name") val name : String,
	@SerializedName("username") val username : String
)