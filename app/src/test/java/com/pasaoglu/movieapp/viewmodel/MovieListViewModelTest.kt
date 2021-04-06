package com.pasaoglu.movieapp.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pasaoglu.movieapp.MainCoroutineRule
import com.pasaoglu.movieapp.data.model.BaseMovieListResponseModel
import com.pasaoglu.movieapp.data.model.MovieListItemResponseModel
import com.pasaoglu.movieapp.data.repository.MainRepository
import com.pasaoglu.movieapp.ui.searchmoviepage.viewmodel.MovieListViewModel
import com.pasaoglu.movieapp.util.Resource
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieListViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private var fakeDetailSuccessFlow = BaseMovieListResponseModel<MovieListItemResponseModel>()
    private val fakeDetailFailureFlow = BaseMovieListResponseModel<MovieListItemResponseModel>()



    @RelaxedMockK
    private lateinit var mockException: Exception

    @RelaxedMockK
    private lateinit var mockUseCase: MainRepository

    private val viewModel: MovieListViewModel by lazy {
        MovieListViewModel(mockUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { mockException.message } returns "Test Exception"
        fakeDetailSuccessFlow.results = mutableListOf()
        for (i in 1..10){
            fakeDetailSuccessFlow.results!!.add(MovieListItemResponseModel())
        }
        fakeDetailSuccessFlow.results
    }


    @Test
    fun `load first page of Movies when getSearchedMovieListWithQuery is called for the first time`() {
        val mockedObserver = createMoviesFeedObserver()

        viewModel.searchResult.observeForever(mockedObserver)
        //  val resource = Resource.Success(mutableListOf<PopularTvShowItem>())

        coEvery { mockUseCase.getMovieListWithQuery("matrix", 1) } returns fakeDetailSuccessFlow


        runBlocking {
            viewModel.getSearchedMovieListWithQuery("matrix", true)
            verifyOrder {
                mockedObserver.onChanged(Resource.loading(data = null))
                //mockedObserver.onChanged(Resource.loading(data = null))
                mockedObserver.onChanged(Resource.success(data = fakeDetailSuccessFlow.results!!.toList()))
            }
        }
      
    }

    @Test
    fun `when load Movies service throws network failure then ViewState renders failure`() {

        val mockedObserver = createMoviesFeedObserver()

        viewModel.searchResult.observeForever(mockedObserver)
        //  val resource = Resource.Success(mutableListOf<PopularTvShowItem>())

        coEvery { mockUseCase.getMovieListWithQuery("matrix", 1) } returns fakeDetailFailureFlow


        runBlocking {
            viewModel.getSearchedMovieListWithQuery("matrix", true)
            verifyOrder {
                mockedObserver.onChanged(Resource.loading(data = null))
                mockedObserver.onChanged(Resource.error(data = null, message = "Error Occurred!"))
            }
        }
    }



    @Test
    fun `when list scrolled to the bottom and error in service call error propagated to view`() {
        // Given
        val mockedObserver = createMoviesFeedObserver()

        viewModel.searchResult.observeForever(mockedObserver)
        //  val resource = Resource.Success(mutableListOf<PopularTvShowItem>())

        coEvery { mockUseCase.getMovieListWithQuery("matrix", 1) } returns fakeDetailFailureFlow


        runBlocking {
            viewModel.getSearchedMovieListWithQuery("matrix", true)
            verifyOrder {
                mockedObserver.onChanged(Resource.loading(data = null))
                mockedObserver.onChanged (Resource.error(data = null, message = "Error Occurred!"))
            }
        }
    }

    private fun createMoviesFeedObserver(): Observer<Resource<List<MovieListItemResponseModel>>> =
        spyk(Observer { })
}