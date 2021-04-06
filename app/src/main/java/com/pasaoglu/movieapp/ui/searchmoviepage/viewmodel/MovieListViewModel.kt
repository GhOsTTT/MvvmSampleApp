package com.pasaoglu.movieapp.ui.searchmoviepage.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasaoglu.movieapp.data.model.MovieListItemResponseModel
import com.pasaoglu.movieapp.data.repository.MainRepository
import com.pasaoglu.movieapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieListViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel(), LifecycleObserver {
    private var _searchScope: Job? = null
    private var _tempMovieListResponse: MutableList<MovieListItemResponseModel> = mutableListOf()
    var searchResult: MutableLiveData<Resource<MutableList<MovieListItemResponseModel>>> = MutableLiveData<Resource<MutableList<MovieListItemResponseModel>>>()
    private var _query = ""
    private var _currentPage = 1
    private var _totalPage = 0


    fun getSearchedMovieListWithQuery(query: String,isEditing:Boolean) {
        println("query = $query , _query = $_query , isEditing = $isEditing , _currentPage = $_currentPage , _totalPage = $_totalPage")
        if(query == _query && isEditing){
            return
        }else if(query != _query && isEditing){
            _searchScope?.cancel()
            _tempMovieListResponse = mutableListOf()
            _currentPage = 1
        }else if(_currentPage < _totalPage && !isEditing){
            _currentPage += 1
        }else{
            return
        }
        _query = query
        if (query.isBlank() || query.length <= 2) {
            searchResult.postValue(Resource.empty(null))
        } else {
           _searchScope =  viewModelScope.launch(Dispatchers.IO) {
                searchResult.postValue(Resource.loading(data = null))
                try {
                    val temp = mainRepository.getMovieListWithQuery(query = query, page = _currentPage)
                    _totalPage = temp.totalPage ?: 0
                    _tempMovieListResponse.addAll(temp.results!!)
                    searchResult.postValue(Resource.success(_tempMovieListResponse))
                } catch (exception: Exception) {
                    // exception.printStackTrace()
                    searchResult.postValue(
                        Resource.error(
                            data = null,
                            message = exception.message ?: "Error Occurred!"
                        )
                    )
                }
            }
        }
    }

}