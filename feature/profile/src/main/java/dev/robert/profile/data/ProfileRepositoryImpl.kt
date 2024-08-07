package dev.robert.profile.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.profile.domain.model.Profile
import dev.robert.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val database: FirebaseFirestore,
    private val mAuth: FirebaseAuth
) : ProfileRepository {
    override fun getProfile(): Flow<Profile> = flow {
        database.collection(COLLECTION_PATH)
            .document(mAuth.uid!!)
            .get()
            .await()
            .toObject(Profile::class.java)
            ?.let { emit(it) }
    }

    companion object {
        const val COLLECTION_PATH = "users"
    }
}
