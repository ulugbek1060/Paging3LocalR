package com.android.paging3test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.paging3test.model.User
import com.android.paging3test.model.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel(
   private val usersRepository: UsersRepository
) : ViewModel() {

   val isErrorsEnabled: Flow<Boolean> = usersRepository.isErrorsEnable()

   val usersFlow: Flow<PagingData<User>>

   private val searchBy = MutableLiveData("")

   init {
      usersFlow = searchBy.asFlow()
         // if user types too quickly -> filtering intermediate values to avoid excess load
         .debounce(500)
         .flatMapLatest {
            usersRepository.getPagedUsers(it)
         }
         // always use cacheIn operator for flows returned by Pager. Otherwise exception may be thrown
         // when 1) refreshing/invalidating or 2) subscribing to the flow more than once.
         .cachedIn(viewModelScope)
   }

   fun setSearchBy(value: String) {
      if (value == searchBy.value) return
      searchBy.value = value
   }

   fun refresh() {
      this.searchBy.postValue(this.searchBy.value)
   }

   fun setEnableErrors(value: Boolean) {
      // called when 'Enable Errors' checkbox value is changed
      usersRepository.setErrorsEnabled(value)
   }

}