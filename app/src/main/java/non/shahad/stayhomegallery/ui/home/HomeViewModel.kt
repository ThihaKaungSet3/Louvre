package non.shahad.stayhomegallery.ui.home

import androidx.lifecycle.*
import com.dropbox.android.external.store4.*
import kotlinx.coroutines.*
import non.shahad.stayhomegallery.data.db.entity.Post
import non.shahad.stayhomegallery.utils.configs.UnsplashConfig
import non.shahad.stayhomegallery.utils.ext.timberD
import non.shahad.stayhomegallery.utils.prefs.SharedPrefHelper
import javax.inject.Inject
import javax.inject.Named

class HomeViewModel @Inject constructor(
    @Named("unsplashPostStore") val unsplashStore : Store<Pair<Long,UnsplashConfig>,List<Post>>,
    private val pref: SharedPrefHelper
) : ViewModel() {

    var isLoading = false

    val unsplashResponse = MutableLiveData<List<Post>>()

    val freshResponse = MutableLiveData<List<Post>>()

    val error = MutableLiveData<String>()

    private val errorExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        timberD("GG_","${throwable.message}")
        error.postValue(throwable.message)
    }

    fun fetchUnsplashByOrder(page: Long){
        isLoading = true
        viewModelScope.launch(errorExceptionHandler) {
            withContext(Dispatchers.IO){
                timberD("HomeViewModel_","${pref.getOrder()}")
                val config = Pair(page, UnsplashConfig(pref.getOrder()))

                val cache = unsplashStore.get(config)
                unsplashResponse.postValue(cache)

                if (cache.isNullOrEmpty()){
                    val remote = unsplashStore.fresh(config)
                    unsplashResponse.postValue(remote)
                }
                isLoading = false
            }
        }
    }

    fun fetchFresh(page: Long){
        isLoading = true
        viewModelScope.launch(errorExceptionHandler) {
            withContext(Dispatchers.IO){
                val config = Pair(page, UnsplashConfig(pref.getOrder()))
                val fresh = unsplashStore.fresh(config)
                freshResponse.postValue(fresh)
                isLoading = false
            }
        }
    }

    fun putOrder(orderBy: String){
        pref.putOrder(orderBy)
    }



}