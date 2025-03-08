package com.example.manganese.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

const val db_REF = "users"





class firedb(){

    val user = Firebase.auth.currentUser
    fun hasUser():Boolean = Firebase.auth.currentUser !=null
    fun getUserid():String =Firebase.auth.currentUser?.uid.orEmpty()

    private val dbUserREF: DocumentReference = Firebase.firestore.collection(db_REF).document(getUserid())

    fun getUserNickname(): Flow<Resources<String>> = flow {
        Log.d("fire db", "Fetching nickname...")
        emit(Resources.Loading())  // Emit loading state
        try {
            val snapshot = dbUserREF.get().await()  // Fetch data once
            val nickname = snapshot.getString("username") ?: "Unknown"
            Log.d("fire db", nickname)
            emit(Resources.Success(nickname))  // Emit success
        } catch (e: Exception) {
            emit(Resources.Error(e))
            Log.d("fire db", "Error fetching nickname")
        }
    }

    fun getUserWatchlist(): Flow<Resources<List<Int>>> = callbackFlow {
        var snapshotStateListener:ListenerRegistration? = null
        try {
            snapshotStateListener = dbUserREF
                .addSnapshotListener{snapshot,error->
                    val response = if(snapshot!=null){
                        val watchList = snapshot.get("watchlist")  as? Map<String, Any> ?: emptyMap()
                        val keys = watchList.keys.mapNotNull { it.toIntOrNull() }
                        Resources.Success(keys)
                    }else{
                        Resources.Error(error?.cause)
                    }
                    trySend(response)
                }

        }
        catch(e:Exception){
            //nothing
            trySend(Resources.Error(e?.cause))
            Log.d("fire db","Error bomboclatt")
            e.printStackTrace()
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }
    fun getUserReadlist(): Flow<Resources<List<Int>>> = callbackFlow {
        var snapshotStateListener:ListenerRegistration? = null
        try {
            snapshotStateListener = dbUserREF
                .addSnapshotListener{snapshot,error->
                    val response = if(snapshot!=null){
                        val readList = snapshot.get("readlist")  as? Map<String, Any> ?: emptyMap()
                        val keys = readList.keys.mapNotNull { it.toIntOrNull() }
                        Resources.Success(keys)
                    }else{
                        Resources.Error(error?.cause)
                    }
                    trySend(response)
                }

        }
        catch(e:Exception){
            //nothing
            trySend(Resources.Error(e?.cause))
            Log.d("fire db","Error bomboclatt")
            e.printStackTrace()
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    suspend fun addToWatchlist(animeId: Int, animeTitle: String): Resources<Unit> {
        return try {

            val watchlistUpdate = mapOf(
                "watchlist.$animeId" to animeTitle  // Firestore supports updating nested fields with this dot notation
            )

            dbUserREF.update(watchlistUpdate).await()

            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }
    suspend fun removeFromWatchlist(animeId: Int): Resources<Unit> {
        return try {

            val watchlistUpdate = mapOf(
                "watchlist.$animeId" to FieldValue.delete()  // Deletes this entry from the map
            )

            dbUserREF.update(watchlistUpdate).await()

            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }
    suspend fun addToReadlist(mangaId: Int, mangaTitle: String): Resources<Unit> {
        return try {

            val readlistUpdate = mapOf(
                "readlist.$mangaId" to mangaTitle  // Firestore supports updating nested fields with this dot notation
            )

            dbUserREF.update(readlistUpdate).await()

            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }
    suspend fun removeFromReadlist(mangaId: Int): Resources<Unit> {
        return try {

            val readlistUpdate = mapOf(
                "readlist.$mangaId" to FieldValue.delete()  // Deletes this entry from the map
            )

            dbUserREF.update(readlistUpdate).await()

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
