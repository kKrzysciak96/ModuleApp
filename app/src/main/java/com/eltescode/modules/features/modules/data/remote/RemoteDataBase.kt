package com.eltescode.modules.features.modules.data.remote

interface RemoteDataBase<T> {

    suspend fun pushModulesToRemote(modules: List<T>)
    suspend fun fetchModulesFromRemote(): List<T>

}