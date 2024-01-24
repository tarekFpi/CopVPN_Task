package com.example.mytodolist.di

import android.content.Context
import androidx.room.Room
import com.example.mytodolist.db.TaskDAo
import com.example.mytodolist.db.TaskDatabase
import com.example.mytodolist.db.UserDAo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {


    @Singleton
    @Provides
    fun provideTaskDB(@ApplicationContext context : Context) : TaskDatabase {
        return Room.databaseBuilder(context, TaskDatabase::class.java, "DataBase").build()
    }

    @Singleton
    @Provides
    fun getProvideDao(taskDatabase: TaskDatabase) : TaskDAo = taskDatabase.getTaskDao()


    @Singleton
    @Provides
    fun getProvideUserDao(taskDatabase: TaskDatabase) : UserDAo = taskDatabase.userResponseDao()


}


