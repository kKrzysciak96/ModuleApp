package com.eltescode.modules.features.modules.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class RemoteSupaDataBaseImpl(private val client: SupabaseClient) :
    RemoteDataBase<SupabaseSpecificModule> {

    override suspend fun fetchModulesFromRemote(): List<SupabaseSpecificModule> {

//        val dataResponse = client.postgrest["modules"].select()
        val dataResponse = client.postgrest["test"].select()
        val data = dataResponse.decodeList<SupabaseSpecificModule>()
        return data
    }

    override suspend fun pushModulesToRemote(modules: List<SupabaseSpecificModule>) {
//        client.postgrest["modules"].delete { SupabaseSpecificModule::reset isExact true }
//        val result = client.postgrest["modules"].insert(modules)
        client.postgrest["test"].delete { SupabaseSpecificModule::reset isExact true }
        val result = client.postgrest["test"].insert(modules)

    }
}

