package com.eltescode.modules.features.modules.di

import android.app.Application
import androidx.room.Room
import com.eltescode.modules.core.preferences.Preferences
import com.eltescode.modules.features.modules.data.local.ModuleDao
import com.eltescode.modules.features.modules.data.local.ModuleDataBase
import com.eltescode.modules.features.modules.data.repository.ModuleRepositoryImpl
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import com.eltescode.modules.features.modules.domain.use_cases.AddModuleUseCase
import com.eltescode.modules.features.modules.domain.use_cases.AddModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.DeleteModuleUseCase
import com.eltescode.modules.features.modules.domain.use_cases.DropDataBase
import com.eltescode.modules.features.modules.domain.use_cases.FilterModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.GetModuleUseCase
import com.eltescode.modules.features.modules.domain.use_cases.GetModulesUseCase
import com.eltescode.modules.features.modules.domain.use_cases.ModuleUseCases
import com.eltescode.modules.features.modules.presentation.main_screen.MainScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ModulesHiltModule {
    @Provides
    @Singleton
    fun provideModuleDataBase(app: Application): ModuleDataBase {
        return Room.databaseBuilder(
            context = app,
            ModuleDataBase::class.java,
            "data-base"
        ).build()
    }

    @Provides
    @Singleton
    fun provideModuleDao(database: ModuleDataBase): ModuleDao {
        return database.provideDao()
    }

    @Provides
    @Singleton
    fun provideModuleRepository(dao: ModuleDao): ModuleRepository {
        return ModuleRepositoryImpl(dao)
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
        )
    }


    @Provides
    @Singleton
    fun provideMainScreenViewModel(
        preferences: Preferences,
        useCases: ModuleUseCases
    ): MainScreenViewModel =
        MainScreenViewModel(
            preferences = preferences,
            useCases = useCases
        )


}