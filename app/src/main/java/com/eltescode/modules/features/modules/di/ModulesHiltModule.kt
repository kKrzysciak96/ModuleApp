package com.eltescode.modules.features.modules.di

import android.app.Application
import androidx.room.Room
import com.eltescode.modules.features.modules.data.local.ModuleDao
import com.eltescode.modules.features.modules.data.local.ModuleDataBase
import com.eltescode.modules.features.modules.data.remote.RemoteDataBase
import com.eltescode.modules.features.modules.data.remote.RemoteSupaDataBaseImpl
import com.eltescode.modules.features.modules.data.remote.SupabaseSpecificModule
import com.eltescode.modules.features.modules.data.repository.ModuleRepositoryImpl
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import com.eltescode.modules.features.modules.domain.use_cases.AddModuleUseCase
import com.eltescode.modules.features.modules.domain.use_cases.AddModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.DeleteModuleUseCase
import com.eltescode.modules.features.modules.domain.use_cases.DeleteModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.DropDataBase
import com.eltescode.modules.features.modules.domain.use_cases.FetchModulesFromRemoteUseCase
import com.eltescode.modules.features.modules.domain.use_cases.FilterAllModuleNames
import com.eltescode.modules.features.modules.domain.use_cases.FilterModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.GetModuleUseCase
import com.eltescode.modules.features.modules.domain.use_cases.GetModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.ModuleUseCases
import com.eltescode.modules.features.modules.domain.use_cases.PushModulesToRemoteUseCase
import com.eltescode.modules.features.modules.domain.use_cases.UpdateUndoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ModulesHiltModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://vntofffmdpjvqywefchb.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZudG9mZmZtZHBqdnF5d2VmY2hiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDE3MDIyNTQsImV4cCI6MjAxNzI3ODI1NH0.1o-lzmwPyCvV5fVAKzhqD8sHHeZg7vCfT8HZBrD5XII"
        ) {
            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun provideRemoteDataBase(client: SupabaseClient): RemoteDataBase<SupabaseSpecificModule> {
        return RemoteSupaDataBaseImpl(client)
    }


    @Provides
    @Singleton
    fun provideModuleDataBase(app: Application): ModuleDataBase {
        return Room.databaseBuilder(
            context = app, ModuleDataBase::class.java, "data-base"
        ).build()
    }

    @Provides
    @Singleton
    fun provideModuleDao(database: ModuleDataBase): ModuleDao {
        return database.provideDao()
    }

    @Provides
    @Singleton
    fun provideModuleRepository(
        dao: ModuleDao, remoteDataBase: RemoteDataBase<SupabaseSpecificModule>
    ): ModuleRepository {
        return ModuleRepositoryImpl(dao, remoteDataBase)
    }

    @Provides
    @Singleton
    fun provideModuleUseCases(repository: ModuleRepository): ModuleUseCases {
        return ModuleUseCases(
            addModuleUseCase = AddModuleUseCase(repository),
            deleteModuleUseCase = DeleteModuleUseCase(repository),
            getModulesUseCase = GetModulesUseCase(repository),
            dropDataBase = DropDataBase(repository),
            filterModulesUseCase = FilterModulesUseCase(),
            addModulesUseCase = AddModulesUseCase(repository),
            getModuleUseCase = GetModuleUseCase(repository),
            fetchModulesFromRemoteUseCase = FetchModulesFromRemoteUseCase(repository),
            pushModulesToRemoteUseCase = PushModulesToRemoteUseCase(repository),
            filterAllModuleNames = FilterAllModuleNames(repository),
            deleteModulesUseCase = DeleteModulesUseCase(repository),
            updateUndoListUseCase = UpdateUndoListUseCase(),
        )
    }
}
