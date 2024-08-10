package dev.robert.profile.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.profile.domain.model.Profile
import dev.robert.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val database: FirebaseFirestore,
    private val mAuth: FirebaseAuth
) : ProfileRepository {
    override fun getProfileGoogleSignIn(): Flow<Profile> = flow {
        mAuth.currentUser?.let {
            emit(
                Profile(
                    name = it.displayName,
                    email = it.email!!,
                    photoUrl = it.photoUrl.toString(),
                    id = it.uid
                )
            )
        }
    }

    override fun getProfileFirebaseFirestore(): Flow<Result<Profile>> = flow {
        database.collection(COLLECTION_PATH)
            .document(mAuth.uid!!)
            .get()
            .await()
            .toObject(Profile::class.java)?.let { profile -> emit(Result.success(profile)) }
    }.catch {
        emit(Result.failure(it))
    }

    companion object {
        const val COLLECTION_PATH = "users"
    }
}
