package com.example.foodictive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodictive.view.detail.DetailModel

class ViewModelFactory: ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(DetailModel::class.java) -> {
                DetailModel() as T
            }
            else -> throw IllegalArgumentException("unknown model class: " + modelClass.name)
        }
    }
}