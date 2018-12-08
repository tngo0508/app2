package edu.fullerton.cpsc411.assignment_2

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

class MovieDbHelper(context: Context) :  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES2)

        Log.d("table","created");

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {


        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    // INSERT TO TABLE USER IN SQLITE
    fun insertNewUser(username:String, password:String): Boolean {

        val db = writableDatabase
        Log.d("Insert Working","one row effected");

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Tables.User.COLUMN_1,username)
            put(Tables.User.COLUMN_2, password)

        }

        db?.insert(Tables.User.TABLE_USER, null, values);

        db.close()

        return true
    }

    // LOGIN CHECK IF PASSWORD IS CORRECT FROM SQLITE DATABASE
    fun loginUser(username:String, password:String): Boolean {

        val db = readableDatabase
        Log.d("login","testing");
        val cursor = db.rawQuery("SELECT * FROM  " + Tables.User.TABLE_USER + " WHERE "  + Tables.User.COLUMN_1 + " = '" + username + "'" , null)


        cursor?.moveToFirst()
        if( password == cursor.getString(cursor.getColumnIndex(Tables.User.COLUMN_2))){
            return true
            Log.d("PASS c", "PASS WORD CORRECT")
            db.close()

        }else
        {
            db.close()

            return false
            Log.d("PASS c", "PASS WORD INCORRECT")

        }

    }
    // all moive in database
    fun insertNewMoive(title:String, description:String): Boolean {

        val db = writableDatabase
        Log.d("Insert Working","one row effected");

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Tables.Moive.COLUMN_1,title)
            put(Tables.Moive.COLUMN_2, description)

        }

        db?.insert(Tables.Moive.TABLE_MOIVE, null, values);

        db.close()

        return true
    }
    // function get all moive in the database

    fun AllMoive(){

        val db = readableDatabase
        Log.d("ALL MOIVES ","testing");
        val cursor = db.rawQuery("SELECT * FROM  " + Tables.Moive.TABLE_MOIVE  , null)
        val Moives =  ArrayList<MovieModel>()
        with(cursor) {
            while (moveToNext()) {


                val title = getString(getColumnIndexOrThrow(Tables.Moive.COLUMN_1))
                val des = getString(getColumnIndexOrThrow(Tables.Moive.COLUMN_2))
                Moives.add(MovieModel(title,des))
            }
        }

        Log.d("moives", Moives.toString())

    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MoiveRs.db"


        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${Tables.User.TABLE_USER} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${Tables.User.COLUMN_1} TEXT," +
                        "${Tables.User.COLUMN_2} TEXT)"


        private const val SQL_CREATE_ENTRIES2 =
                "CREATE TABLE ${Tables.Moive.TABLE_MOIVE} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${Tables.Moive.COLUMN_1} TEXT," +
                        "${Tables.Moive.COLUMN_2} TEXT)"




        private var sInstance: MovieDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MovieDbHelper {


            if (sInstance == null) {
                sInstance = MovieDbHelper(context.applicationContext)
            }
            return sInstance as MovieDbHelper
        }

    }
}