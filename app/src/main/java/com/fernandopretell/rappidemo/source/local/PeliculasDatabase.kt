package com.fernandopretell.rappidemo.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(ResponseEntity::class), version = 1)
abstract class PeliculasDatabase : RoomDatabase(){

    abstract fun notaDao():PeliculaDao

    companion object{

        private var instance: PeliculasDatabase? = null

        fun getInstance(context: Context):PeliculasDatabase?{

            if(instance == null){
                instance = Room.databaseBuilder<PeliculasDatabase>(context.applicationContext,
                    PeliculasDatabase::class.java,"db_peliculas")
                    .allowMainThreadQueries() //para llamar a base de datos en el hilo principal
                    .addCallback(room)
                    .build()
            }

            return instance
        }

        private val room = object  : RoomDatabase.Callback(){

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                //PopulateDBAsyncTask(instance).execute()
            }
        }
    }



    /*class PopulateDBAsyncTask(instance: PeliculasDatabase?) : AsyncTask<Void,Void,Void>(){

        private val notaDao : PeliculaDao? = instance?.notaDao()

        override fun doInBackground(vararg p0: Void?): Void? {

            notaDao?.insert(ResponseEntity(titulo = "Titulo01",descripcion = "Descripcion01"))
            notaDao?.insert(ResponseEntity(titulo = "Titulo01",descripcion = "Descripcion01"))
            notaDao?.insert(ResponseEntity(titulo = "Titulo01",descripcion = "Descripcion01"))
            notaDao?.insert(ResponseEntity(titulo = "Titulo01",descripcion = "Descripcion01"))
            notaDao?.insert(ResponseEntity(titulo = "Titulo01",descripcion = "Descripcion01"))
            notaDao?.insert(ResponseEntity(titulo = "Titulo01",descripcion = "Descripcion01"))

            return null
        }

    }*/
}