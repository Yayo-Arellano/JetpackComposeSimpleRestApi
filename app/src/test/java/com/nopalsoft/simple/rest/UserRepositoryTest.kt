package com.nopalsoft.simple.rest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nopalsoft.simple.rest.datasource.RestDataSource
import com.nopalsoft.simple.rest.model.User
import com.nopalsoft.simple.rest.model.UserDao
import com.nopalsoft.simple.rest.repository.UserRepositoryImp
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets


private val user1 = User("Yayo", "Arellano", "Taipei", "http://..")
private val user2 = User("Carlos", "Caguamas", "Torreon", "http://..")

class UserRepositoryTest {
    private val mockWebServer = MockWebServer().apply {
        url("/")
        dispatcher = myDispatcher
    }

    private val restDataSource = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RestDataSource::class.java)

    private val newsRepository = UserRepositoryImp(restDataSource, MockUserDao())

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Users on the DB are retrieved correctly`() {
        val users = newsRepository.getAllUsers()
        assertEquals(2, users.value?.size)
    }

    @Test
    fun `Users is deleted correctly`() {
        runBlocking {
            newsRepository.deleteUser(user1)

            val users = newsRepository.getAllUsers()
            assertEquals(1, users.value?.size)
        }
    }

    @Test
    fun `Users is fetched correctly`() {
        runBlocking {
            val newUser = newsRepository.getNewUser()

            val users = newsRepository.getAllUsers()
            assertEquals(3, users.value?.size)
            assertEquals(newUser.name, "Wayne")
            assertEquals(newUser.lastName, "Collins")
            assertEquals(newUser.city, "Cairns")
            assert(newUser.thumbnail.contains("thumb/women/78.jpg"))
        }
    }
}

class MockUserDao : UserDao {

    private val users = MutableLiveData(listOf(user1, user2));

    override fun insert(user: User) {
        users.value = users.value?.toMutableList()?.apply { add(user) }
    }

    override fun getAll(): LiveData<List<User>> = users

    override fun delete(user: User) {
        users.value = users.value?.toMutableList()?.apply { remove(user) }
    }
}

val myDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/?inc=name" -> MockResponse().apply { addResponse("api_name.json") }
            "/?inc=location" -> MockResponse().apply { addResponse("api_location.json") }
            "/?inc=picture" -> MockResponse().apply { addResponse("api_picture.json") }
            else -> MockResponse().setResponseCode(404)
        }
    }
}

fun MockResponse.addResponse(filePath: String): MockResponse {
    val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
    val source = inputStream?.source()?.buffer()
    source?.let {
        setResponseCode(200)
        setBody(it.readString(StandardCharsets.UTF_8))
    }
    return this
}