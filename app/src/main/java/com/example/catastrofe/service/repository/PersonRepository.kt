package com.example.catastrofe.service.repository

import android.content.Context
import com.example.catastrofe.R
import com.example.catastrofe.service.listener.APIListener
import com.example.catastrofe.service.model.PersonModel
import com.example.catastrofe.service.repository.remote.PersonService
import com.example.catastrofe.service.repository.remote.RetrofitClient

class PersonRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        executeCall(remote.login(email, password), listener)
    }

    fun create(name: String, email: String, password: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        executeCall(remote.create(name, email, password), listener)
    }

}