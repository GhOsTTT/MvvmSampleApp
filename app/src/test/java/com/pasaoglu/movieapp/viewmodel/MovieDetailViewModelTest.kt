package com.pasaoglu.movieapp.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pasaoglu.movieapp.MainCoroutineRule
import com.pasaoglu.movieapp.data.model.MovieDetailResponseModel
import com.pasaoglu.movieapp.data.repository.MainRepository
import com.pasaoglu.movieapp.ui.moviedetailpage.viewmodel.MovieDetailViewModel
import com.pasaoglu.movieapp.util.Resource
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private var fakeDetailSuccessFlow: MovieDetailResponseModel? = null
    private var fakeDetailErrorFlow= Resource.error(data = null, message = "")


    @RelaxedMockK
    private lateinit var mockException: Exception

    @RelaxedMockK
    private lateinit var mockUseCase: MainRepository

    private val viewModel: MovieDetailViewModel by lazy {
        MovieDetailViewModel(mockUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        fakeDetailSuccessFlow = MovieDetailResponseModel("imagePath","overView","voteAverage",null)
        every { mockException.message } returns "MovieDetailViewModelTest Test Exception"
    }


    @Test
    fun `load movie Detail page`() {
        val mockedObserver = createMovieDetailObserver()
        viewModel.getMovieDetailWithMovieId(1).observeForever(mockedObserver)
        coEvery {
            mockUseCase.getMovieDetailWithMovieId( 1)
        } returns fakeDetailSuccessFlow!!
        runBlocking {
            viewModel.getMovieDetailWithMovieId(1)
            coVerify {
                //mockedObserver.onChanged(Resource.loading(data = null))
                //mockedObserver.onChanged(MovieDetailResponseModel)
                mockedObserver.onChanged(any())
                //mockedObserver.onChanged(Resource.error(data = null,message = "asd"))
            }
        }
      
    }

    private fun createMovieDetailObserver(): Observer<Resource<MovieDetailResponseModel>> =
        spyk(Observer {
            fakeDetailErrorFlow
        })
}