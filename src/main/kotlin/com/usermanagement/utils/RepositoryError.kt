package com.usermanagement.utils

sealed class RepositoryError(override val message: String) : Throwable() {
    object UserNotFound : RepositoryError(message = "User not found")
    object UserNotCreated : RepositoryError(message = "Could not create user")
    object UserEmailInUse : RepositoryError(message = "User email already in use")
}
