package com.example.mobile_app.data.repository

import com.example.mobile_app.data.api.RetrofitInstance
import com.example.mobile_app.data.model.User

class UserRepository {
    suspend fun getUsers(gender: String?):List<User>{
        return RetrofitInstance.api.getUsers(10, gender).results
    }
}