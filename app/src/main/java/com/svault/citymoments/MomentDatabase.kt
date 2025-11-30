package com.svault.citymoments

import android.app.Application
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [ModelMoment::class],
    version = 1,
    exportSchema = false
)
abstract class MomentDatabase : RoomDatabase() {
    companion object {
        fun initDatabase(application: Application) = Room.databaseBuilder(
            application, MomentDatabase::class.java, "moment_database"
        ).build()
    }

    abstract fun momentDao(): MomentDao
}

@Dao
interface MomentDao {
    @Query("SELECT * FROM table_moment")
    fun getAllMoments(): Flow<List<ModelMoment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoment(moment: ModelMoment)

    @Delete
    suspend fun deleteMoment(moment: ModelMoment)
}