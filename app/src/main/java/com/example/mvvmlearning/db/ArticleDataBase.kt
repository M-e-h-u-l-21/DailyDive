package com.example.mvvmlearning.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmlearning.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDataBase:RoomDatabase() { //Class must be abstract for room
    abstract fun getArticleDao():ArticleDAO

    companion object{
        @Volatile
        private var instance:ArticleDataBase?=null //Creating instance of database
        private var LOCK=Any() //For synchronization , ensuring only one instance of database is present at any time

        operator fun invoke(context: Context)=instance?:synchronized(LOCK){       //Code in this block can be run by only one thread at a time
            instance?:createDatabase(context).also{
                instance=it
            }
        }//Will be called whenever we create an instance of ArticleDataBase like ArticleDataBase()

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDataBase::class.java,
            "article_db.db"
        ).build()
    }
}