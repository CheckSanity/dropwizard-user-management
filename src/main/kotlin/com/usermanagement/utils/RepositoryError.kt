package com.usermanagement.utils

sealed class RepositoryError(override val message: String) : Throwable() {
    object UserNotFound : RepositoryError(message = "User not found")
    object UserDeleted : RepositoryError(message = "User deleted")
    object UserNotCreated : RepositoryError(message = "Could not create user")
    object UserEmailInUse : RepositoryError(message = "User email already in use")
    object GroupNameInUse : RepositoryError(message = "Group name already in use")
    object GroupNotFound : RepositoryError(message = "Group not found")
    object GroupNotCreated : RepositoryError(message = "Could not create group")
    object GroupDeleted : RepositoryError(message = "Group deleted")
}
