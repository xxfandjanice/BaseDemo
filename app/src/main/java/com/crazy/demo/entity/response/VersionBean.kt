package com.crazy.demo.entity.response


data class VersionBean(
    val need_upgrade : Boolean,
    val version: VersionInfo
)

data class VersionInfo(
    val id: String,
    val version:String,
    val platform:String,
    val download_url: String,
    val message:String
)

