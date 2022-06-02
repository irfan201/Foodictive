package com.example.foodictive.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FoodResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
@Parcelize
data class FoodsItem(

	@field:SerializedName("howtocook")
	val howtocook: String? = null,

	@field:SerializedName("tenth")
	val tenth: String? = null,

	@field:SerializedName("eighth")
	val eighth: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("second")
	val second: String? = null,

	@field:SerializedName("sixth")
	val sixth: String? = null,

	@field:SerializedName("third")
	val third: String? = null,

	@field:SerializedName("seventh")
	val seventh: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ninth")
	val ninth: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("fifth")
	val fifth: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("fourth")
	val fourth: String? = null,

	@field:SerializedName("first")
	val first: String? = null
):Parcelable

data class Data(

	@field:SerializedName("foods")
	val foods: List<FoodsItem?>? = null
)
