package hiretimsf.com.app.repository.repo

import hiretimsf.com.app.repository.network.Result

interface Repository {

    /** MAIN SCREEN ------------------------------------------------------------------------------------------------- */

    /**
     * Fetch data from network and update the database
     */
    suspend fun fetchAll(): Result
}