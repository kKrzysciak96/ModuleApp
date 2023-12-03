package com.eltescode.modules.features.modules.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eltescode.modules.features.modules.domain.model.Module

@Database(entities = [Module::class], version = 1)
abstract class ModuleDataBase : RoomDatabase() {
    abstract fun provideDao(): ModuleDao
}