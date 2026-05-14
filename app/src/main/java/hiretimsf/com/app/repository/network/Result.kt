package hiretimsf.com.app.repository.network

sealed class Result

/** Network request successful */
object Success : Result()

/** Network request failed */
object Failed : Result()