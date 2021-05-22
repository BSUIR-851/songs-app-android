package com.morozov.songs.models.songs

data class SongsAsset (
    val id: String,
    var name: String,
    var author: String,
    var description: String,
    var year: Int,

    var photoFileData: SongsCloudFileData?,
    var videoFileData: SongsCloudFileData?,

    var release: SongsMapRelease?,
)