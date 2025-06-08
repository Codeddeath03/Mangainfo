package com.example.manganese.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

const val db_REF = "users"

class firedb {

    private val user: FirebaseUser?
        get() = Firebase.auth.currentUser


    fun hasUser(): Boolean = user != null
    fun getUserid(): String = user?.uid.orEmpty()
init {
   Log.d("firedb initialized","does it have user rn? ${hasUser()}")
}
    private val dbUserREF: DocumentReference?
        get() = user?.uid?.let { uid ->
            Firebase.firestore.collection(db_REF).document(uid)
        }

    fun getUserNickname(): Flow<Resources<String>> = flow {
        Log.d("fire db", "Fetching nickname...")
        Log.d("fire db", "Current user UID: ${FirebaseAuth.getInstance().currentUser?.uid}")
        Log.d("fire db", "User doc path: ${dbUserREF?.path}")
        emit(Resources.Loading())

        val ref = dbUserREF
        if (ref == null) {
            emit(Resources.Error(Exception("User not logged in")))
            Log.d("fire db", "Not signed in yet")
            return@flow
        }
            val snapshot = ref.get().await()
            val nickname = snapshot.getString("username") ?: "Unknown"
            Log.d("fire db", "Nickname: $nickname")
            emit(Resources.Success(nickname))

    } .catch {e ->
        Log.e("fire db", "Error fetching nickname", e)
        emit(Resources.Error(e))
    }


    fun getUserWatchlist(): Flow<Resources<List<Int>>> = callbackFlow {
        val ref = dbUserREF
        if (ref == null) {
            Log.d("fire db watchlist","User not logged in")
            trySend(Resources.Error(Exception("User not logged in")))
            close()
            return@callbackFlow
        }

        val listener = ref.addSnapshotListener { snapshot, error ->
            Log.d("WatchlistFireDB", "Snapshot listener triggered $error")
            if (error != null) {
                // Real error occurred
                trySend(Resources.Error(error)).isSuccess
                return@addSnapshotListener
            }

//            val response = if (snapshot != null && snapshot.exists()) {
//                val map = snapshot.data?.get("watchlist") as? Map<String, Any> ?: emptyMap()
//                val keys = map.keys.mapNotNull { it.toIntOrNull() }
//                Resources.Success(keys)
//            } else {
//                Resources.Error(error ?: Exception("Snapshot is null"))
//            }
//            trySend(response).isSuccess
            if (snapshot != null && snapshot.exists()) {
                val map = snapshot.data?.get("watchlist") as? Map<String, Any> ?: emptyMap()
                val keys = map.keys.mapNotNull { it.toIntOrNull() }
                trySend(Resources.Success(keys)).isSuccess
            } else {
                // Document does not exist, treat as empty watchlist instead of error
                trySend(Resources.Success(emptyList())).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }
    fun getUserReadlist(): Flow<Resources<List<Int>>> = callbackFlow {

        val ref = dbUserREF
        if (ref == null) {
            Log.d("fire db readlist","User not logged in")
            trySend(Resources.Error(Exception("User not logged in")))
            close()
            return@callbackFlow
        }

        val listener = ref.addSnapshotListener { snapshot, error ->
            Log.d("ReadlistFireDB", "Snapshot listener triggered $error")
            if (error != null) {
                // Real error occurred
                trySend(Resources.Error(error)).isSuccess
                return@addSnapshotListener
            }
//            val response = if (snapshot != null && snapshot.exists()) {
//                val map = snapshot.data?.get("readlist") as? Map<String, Any> ?: emptyMap()
//                val keys = map.keys.mapNotNull { it.toIntOrNull() }
//                Resources.Success(keys)
//            } else {
//                Resources.Error(error ?: Exception("Snapshot is null"))
//            }
//            trySend(response).isSuccess
            if (snapshot != null && snapshot.exists()) {
                val map = snapshot.data?.get("readlist") as? Map<String, Any> ?: emptyMap()
                val keys = map.keys.mapNotNull { it.toIntOrNull() }
                trySend(Resources.Success(keys)).isSuccess
            } else {
                // Document does not exist, treat as empty watchlist instead of error
                trySend(Resources.Success(emptyList())).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    suspend fun createUser(email:String,password:String,nickname: String):Resources<Unit>  {
        val user = Firebase.auth.currentUser
        if (user == null) {
            return Resources.Error(Exception("No authenticated user found"))
        }
        return try{
            Log.d("register_user","$email $password $nickname")
                val dict = hashMapOf(
                "username" to nickname,
                "watchlist" to hashMapOf<String,Any>(),
                "readlist" to hashMapOf<String,Any>(),
                "likedAnimes" to hashMapOf<String,Any>(),
                "likedMangas" to hashMapOf<String,Any>()
                )
                Firebase.firestore.collection(db_REF).document(user.uid).set(dict).await()
                Resources.Success(Unit)

        }
        catch (e:Exception){
            Log.d("register_user err","$e")
            Resources.Error(e)
        }
    }


    suspend fun addToWatchlist(animeId: Int, animeTitle: String): Resources<Unit> {
        val ref = dbUserREF ?: return Resources.Error(Exception("User not logged in"))
        return try {
            val update = mapOf("watchlist.$animeId" to animeTitle)
            ref.update(update).await()
            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }





    suspend fun removeFromWatchlist(animeId: Int): Resources<Unit> {
        val ref = dbUserREF ?: return Resources.Error(Exception("User not logged in"))
        return try {
            val update = mapOf("watchlist.$animeId" to FieldValue.delete())
            ref.update(update).await()
            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }

    suspend fun addToReadlist(mangaId: Int, mangaTitle: String): Resources<Unit> {
        val ref = dbUserREF ?: return Resources.Error(Exception("User not logged in"))
        return try {
            val update = mapOf("readlist.$mangaId" to mangaTitle)
            ref.update(update).await()
            Log.d("readlist","crazy")
            Resources.Success(Unit)
        } catch (e: Exception) {
            Log.d("readlist","$e")
            Resources.Error(e)
        }
    }

    suspend fun removeFromReadlist(mangaId: Int): Resources<Unit> {
        val ref = dbUserREF ?: return Resources.Error(Exception("User not logged in"))
        return try {
            val update = mapOf("readlist.$mangaId" to FieldValue.delete())
            ref.update(update).await()
            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }
}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
}
