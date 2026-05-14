package hiretimsf.com.app.repository.repo

import hiretimsf.com.app.repository.network.Result
import hiretimsf.com.app.repository.network.Success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImp @Inject constructor() : Repository {
    override suspend fun fetchAll(): Result = Success
}
