package com.example.catastrofe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catastrofe.service.constants.CatastrofeConstants
import com.example.catastrofe.service.listener.APIListener
import com.example.catastrofe.service.model.CatastrofeModel
import com.example.catastrofe.service.model.ValidationModel
import com.example.catastrofe.service.repository.PriorityRepository
import com.example.catastrofe.service.repository.CatastrofeRepository

class CatastrofeListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = CatastrofeRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<CatastrofeModel>>()
    val tasks: LiveData<List<CatastrofeModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun list(filter: Int) {
        taskFilter = filter
        val listener = object : APIListener<List<CatastrofeModel>> {
            override fun onSuccess(result: List<CatastrofeModel>) {
                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }
                _tasks.value = result
            }
            override fun onFailure(message: String) {}
        }

        when (filter) {
            CatastrofeConstants.FILTER.ALL -> taskRepository.list(listener)
            CatastrofeConstants.FILTER.NEXT -> taskRepository.listNext(listener)
            else -> taskRepository.listOverdue(listener)
        }
    }

    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        })
    }

    fun status(id: Int, complete: Boolean) {
        val listener = object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }
        }

        if (complete) {
            taskRepository.complete(id, listener)
        } else {
            taskRepository.undo(id, listener)
        }
    }

}