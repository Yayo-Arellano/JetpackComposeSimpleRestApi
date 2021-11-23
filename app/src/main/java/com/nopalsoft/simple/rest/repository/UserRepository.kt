package com.nopalsoft.simple.rest.repository

import androidx.lifecycle.LiveData
import com.nopalsoft.simple.rest.datasource.RestDataSource
import com.nopalsoft.simple.rest.model.User
import com.nopalsoft.simple.rest.model.UserDao
import kotlinx.coroutines.delay
import javax.inject.Inject

interface UserRepository {
    suspend fun getNewUser(): User
    suspend fun deleteUser(toDelete: User)
    fun getAllUsers(): LiveData<List<User>>

}

class UserRepositoryImp @Inject constructor(
    private val dataSource: RestDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getNewUser(): User {
        delay(3000)
        val name = dataSource.getUserName().results.getOrNull(0)?.name
        val location = dataSource.getUserLocation().results.getOrNull(0)?.location
        val picture = dataSource.getUserPicture().results.getOrNull(0)?.picture
        val user = User(name?.first.toString(), name?.last.toString(), location?.city.toString(), picture?.thumbnail.toString())
        userDao.insert(user)
        return user
    }

    override fun getAllUsers() = userDao.getAll()

    override suspend fun deleteUser(toDelete: User) = userDao.delete(toDelete)

}
