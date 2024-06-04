package com.example.wildan_canary.sigin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.wildan_canary.helper.repository.UniversalRepository
import com.example.wildan_canary.view.sig.`in`.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.example.wildan_canary.data.network.response.Result
import com.example.wildan_canary.data.network.response.auth.sigin.InResponse
import com.example.wildan_canary.helper.DataDUmmy
import com.example.wildan_canary.helper.getOrAwaitValue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: UniversalRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDUmmy.generateDummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when login Should Not Null and return success`() {
        val expectedLoginResponse = MutableLiveData<Result<InResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(storyRepository.postLogin(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.loginAccount(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postLogin(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLoginResponse = MutableLiveData<Result<InResponse>>()
        expectedLoginResponse.value = Result.Error("network error")
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(storyRepository.postLogin(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.loginAccount(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).postLogin(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}