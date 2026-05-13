package me.tumur.portfolio.repository.repo

import me.tumur.portfolio.repository.network.Result
import me.tumur.portfolio.repository.network.Success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImp @Inject constructor() : Repository {
    override suspend fun fetchAll(): Result = Success
}
