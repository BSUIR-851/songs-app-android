package com.morozov.songs.services

import java.lang.Exception

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

import com.morozov.songs.models.songs.auth.SongsAuth
import com.morozov.songs.models.songs.SongsAsset
import com.morozov.songs.models.songs.SongsDashboard
import com.morozov.songs.models.songs.auth.SongsUser
import com.morozov.songs.models.songs.auth.UsersDashboard

object SessionService {

    private var songsAuth: SongsAuth? = null
    private var dashboard: SongsDashboard? = null

    private var initialized: Boolean = false

    private var songsUser: SongsUser? = null
    private var usersDashboard: UsersDashboard? = UsersDashboard()

    private val songsUserService = SongsUserFirebaseService()
    private val songsAssetFirebaseService = SongsAssetFirebaseService()

    var selectedAsset: SongsAsset? = null
    var selectedUser: SongsUser? = null

    fun isInitialized(): Boolean = initialized

    fun getLocalAssets(): ArrayList<SongsAsset>? {
        return dashboard?.assets
    }

    fun getLocalAsset(id: String): SongsAsset? {
        return getLocalAssets()?.first { asset ->
            asset.id == id
        }
    }

    private fun deleteLocalAsset(asset: SongsAsset) {
        val index = getLocalAssets()?.indexOfFirst { asset0 ->
            asset0.id == asset.id
        }
        if (index != null && index != -1) {
            dashboard?.assets?.removeAt(index)
            if (selectedAsset?.id == asset.id) {
                selectedAsset = null
            }
        }
    }

    fun deleteRemoteAsset(asset: SongsAsset, completion: (Exception?) -> Unit) {
        songsAssetFirebaseService.deleteRemoteAsset(asset) { error ->
            if (error != null) {
                println(error)
                completion(error)
            } else {
                this.deleteLocalAsset(asset)
                completion(null)
            }
        }
    }

    private fun updateLocalAsset(asset: SongsAsset) {
        val index = getLocalAssets()?.indexOfFirst { asset0 ->
            asset0.id == asset.id
        }
        if (index != null && index != -1) {
            dashboard?.assets?.set(index, asset)
            if (selectedAsset?.id == asset.id) {
                selectedAsset = asset
            }
        } else {
            dashboard?.assets?.add(asset)
        }
    }

    fun updateRemoteAsset(asset: SongsAsset, photoUri: Uri?, videoUri: Uri?, completion: (Exception?) -> Unit) {
        songsAssetFirebaseService.updateRemoteAsset(asset, photoUri, videoUri) { updatedAsset, error ->
            if (error != null) {
                println(error)
                completion(error)
            } else if (updatedAsset != null) {
                this.updateLocalAsset(updatedAsset)
                completion(null)
            } else {
                completion(Exception("Invalid updateRemoteAsset form SongsAssetFirebaseService closure return"))
            }
        }
    }

    fun syncDashboard(onCompleted: () -> Unit) {
        songsAssetFirebaseService.getRemoteAssets { assets, error ->
            if (error != null) {
                println(error)
                this.dashboard?.assets = ArrayList()
                this.selectedAsset = null
            } else if (assets != null) {
                this.dashboard?.assets = assets
                if (selectedAsset != null) {
                    this.selectedAsset = getLocalAsset(selectedAsset!!.id)
                }
            } else {
                println("Didn't receive assets and error")
                this.dashboard?.assets = ArrayList()
                this.selectedAsset = null
            }
            onCompleted()
        }
    }

    private fun initialize(songsAuth: SongsAuth, onCompleted: () -> Unit) {
        this.songsAuth = songsAuth

        AuthSongsStorageService.save(songsAuth)

        dashboard = SongsDashboard()

        syncDashboard {
            this.initialized = true
            onCompleted()
        }

    }

    fun destroy() {
        initialized = false

        try {
            FirebaseAuth.getInstance().signOut()
        } catch (error: Throwable) {
            println(error)
        }

        AuthSongsStorageService.delete()
        selectedAsset = null
        selectedUser = null
        songsAuth = null
        dashboard = null
        songsUser = null
        usersDashboard = UsersDashboard()
    }

    fun restore(completion: (Exception?) -> Unit): SongsAuth? {
        val songsAuth = AuthSongsStorageService.restore()
        if (songsAuth != null) {
            signInEmail(songsAuth.email, songsAuth.password) { error ->
                this.handleFirebaseAuthResponse(songsAuth, error, completion)
            }
            return songsAuth
        } else {
            completion(Exception("Unable to restore session"))
            return null
        }
    }

    fun signUpEmail(email: String, password: String, completion: (Exception?) -> Unit) {
        val songsAuth = SongsAuth(email, password)
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { _ ->
                this.handleFirebaseAuthResponse(songsAuth, null, completion)
                this.updateRemoteUserAuth(songsAuth, null, completion)
            }
            .addOnFailureListener { e ->
                this.handleFirebaseAuthResponse(songsAuth, e, completion)
                this.updateRemoteUserAuth(songsAuth, e, completion)
            }
    }

    fun signInEmail(email: String, password: String, completion: (Exception?) -> Unit) {
        val songsAuth = SongsAuth(email, password)
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _ ->
                this.handleFirebaseAuthResponse(songsAuth, null, completion)
            }
            .addOnFailureListener { e ->
                this.handleFirebaseAuthResponse(songsAuth, e, completion)
            }
    }

    fun getSongsUser(): SongsUser? {
        return songsUser
    }

    fun getSongsUsers(): ArrayList<SongsUser>? {
        return usersDashboard?.users
    }

    fun getSongsUserById(id: String): SongsUser? {
        return getSongsUsers()?.first { user ->
            user.id == id
        }
    }

    fun syncUsersDashboard(onCompleted: () -> Unit) {
        songsUserService.getRemoteUsers { users, error ->
            if (error != null) {
                println(error)
                this.usersDashboard?.users = ArrayList()
                this.selectedUser = null
            } else if (users != null) {
                users.removeIf { user ->
                    user.id == this.getSongsUser()?.id
                }
                this.usersDashboard?.users = users
                if (selectedUser != null) {
                    this.selectedUser = getSongsUserById(selectedUser!!.id)
                }
            } else {
                println("Didn't receive any users and error")
                this.usersDashboard?.users = ArrayList()
                this.selectedUser = null
            }
            onCompleted()
        }
    }

    fun getRemoteUser(songsAuth: SongsAuth, completion: (SongsUser?, Exception?) -> Unit) {
        val currUser = Firebase.auth.currentUser
        if (currUser != null) {
            val userID = currUser.uid
            val user = SongsUser(userID, songsAuth.email)
            songsUserService.getRemoteUser(user) { recvUserFromDB, e ->
                if (e != null) {
                    println(e)
                    completion(null, e)
                } else if (recvUserFromDB != null) {
                    completion(recvUserFromDB, null)
                } else {
                    completion(null, Exception("Invalid getRemoteUser form SongsUserFirebaseService closure return"))
                }
            }
        } else {
            completion(null, Exception("Invalid getRemoteUser form SongsUserFirebaseService closure return"))
        }
    }

    fun updateRemoteUserAuth(songsAuth: SongsAuth, error: Exception?, completion: (Exception?) -> Unit) {
        if (error != null) {
            completion(error)
            return
        }
        val currUser = Firebase.auth.currentUser
        if (currUser != null) {
            val userID = currUser.uid
            val user = SongsUser(userID, songsAuth.email)
            updateRemoteUser(user, completion)
        } else {
            completion(Exception("Invalid updateRemoteUserAuth form SongsUserFirebaseService closure return"))
            return
        }
    }

    fun updateRemoteUser(user: SongsUser, completion: (Exception?) -> Unit) {
        val index = usersDashboard?.users?.indexOfFirst { findUser ->
            findUser.id == user.id
        }
        if (index != null) {
            if (index != -1) {
                usersDashboard?.users?.set(index, user)
            }
        } else {
            usersDashboard?.users?.add(user)
        }

        songsUserService.updateRemoteUser(user) { updatedUser, error ->
            if (error != null) {
                println(error)
                completion(error)
            } else if (updatedUser != null) {
                completion(null)
            } else {
                completion(Exception("Invalid updateRemoteUser form SongsUserFirebaseService closure return"))
            }
        }
    }

    private fun handleFirebaseAuthResponse(songsAuth: SongsAuth, error: Exception?, completion: (Exception?) -> Unit) {
        if (error != null) {
            completion(error)
            return
        }

        initialize(songsAuth) {
            if (this.initialized) {
                this.getRemoteUser(songsAuth) { user, e ->
                    if (e != null) {
                        completion(e)
                    } else {
                        songsUser = user
                        if (user?.isAdmin == true) {
                            syncUsersDashboard() {}
                        }
                    }
                    completion(null)
                }
            } else {
                completion(Exception("Unable to initialize session"))
            }
        }
    }

}