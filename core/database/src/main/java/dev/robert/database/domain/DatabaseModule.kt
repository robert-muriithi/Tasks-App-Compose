package dev.robert.database.domain

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.database.ConstUtils.TODO_DATABASE
import dev.robert.database.data.TodoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @[
        Provides
        Singleton
    ]
    fun provideGson () = Gson()

    @[
        Provides
        Singleton
    ]
    fun provideTodoDao(db: TodoDatabase) = db.dao

    @[
        Provides
        Singleton
    ]
    fun provideTodoDatabase(@ApplicationContext context: Context) : TodoDatabase {
        return Room.databaseBuilder(
            context = context,
            TodoDatabase::class.java,
            TODO_DATABASE
        ).build()
    }
}