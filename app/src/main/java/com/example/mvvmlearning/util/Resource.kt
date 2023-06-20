package com.example.mvvmlearning.util

//Sealed classes are just like abstract classes but they help us define which classes can inherit from the sealed class
sealed class Resource<T>(
    val data: T? =null,
    val message:String?=null
) {
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(message: String,data: T?=null):Resource<T>(data,message)
    class Loading<T>:Resource<T>()
}