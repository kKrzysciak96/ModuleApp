package com.eltescode.modules.features.modules.data.remote

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class RemoteSupaDataBaseImpl(private val client: SupabaseClient) :
    RemoteDataBase<SupabaseSpecificModule> {

    override suspend fun fetchModulesFromRemote(): List<SupabaseSpecificModule> {

        val dataResponse = client.postgrest["modules"].select()
        val data = dataResponse.decodeList<SupabaseSpecificModule>()
        Log.d("SUPABASE", data.toString())

        return data
    }

    override suspend fun pushModulesToRemote(modules: List<SupabaseSpecificModule>) {
        client.postgrest["modules"].delete {
            SupabaseSpecificModule::reset isExact true
        }
        Log.d("PUSHED REMOTE", modules.toString())
        val result = client.postgrest["modules"].insert(modules)

    }
}

