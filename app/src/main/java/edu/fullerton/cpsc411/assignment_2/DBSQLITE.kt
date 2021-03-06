package edu.fullerton.cpsc411.assignment_2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log


// https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase


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
        if (username != "" && password != "") {
            val db = writableDatabase
            Log.d("Insert Working", "one row effected");

            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(Tables.User.COLUMN_1, username)
                put(Tables.User.COLUMN_2, password)

            }

            val newrow = db?.insert(Tables.User.TABLE_USER, null, values);
            if (newrow == -1.toLong()) {
                Log.d("new row", newrow.toString())

                db.close()
                return false
            } else {
                db.close()



                return true
            }
        }
        return false


    }

    // LOGIN CHECK IF PASSWORD IS CORRECT FROM SQLITE DATABASE
    fun loginUser(username:String, password:String): Boolean {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            val db = readableDatabase
            Log.d("login", "testing");
            val cursor = db.rawQuery("SELECT * FROM  " + Tables.User.TABLE_USER + " WHERE " + Tables.User.COLUMN_1 + " = '" + username + "'", null)

            if (cursor.count > 0) {

                cursor?.moveToFirst()
                if (password == cursor.getString(cursor.getColumnIndex(Tables.User.COLUMN_2))) {
                    Log.d("PASS c", "PASS WORD CORRECT")
                    db.close()

                    return true

                } else {
                    db.close()
                    Log.d("PASS c", "PASS WORD INCORRECT")

                    return false

                }
            }
            return false
        }
        return false

    }
    // all moive in database
    fun insertNewMoive(title:String, description:String , img:String): Boolean {

        val db = writableDatabase
        Log.d("Insert Working","one row effected");

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Tables.Moive.COLUMN_1,title)
            put(Tables.Moive.COLUMN_2, description)
            put(Tables.Moive.COLUMN_3, img)


        }

        db?.insert(Tables.Moive.TABLE_MOIVE, null, values);

        db.close()

        return true
    }

    // function get all moive in the database

    fun AllMoive(): ArrayList<MovieModel> {

        val db = readableDatabase
        Log.d("ALL MOIVES ","testing");
        val cursor = db.rawQuery("SELECT * FROM  " + Tables.Moive.TABLE_MOIVE  , null)
        val Moives =  ArrayList<MovieModel>()
        with(cursor) {
            while (moveToNext()) {


                val title = getString(getColumnIndexOrThrow(Tables.Moive.COLUMN_1))
                val des = getString(getColumnIndexOrThrow(Tables.Moive.COLUMN_2))
                val img = getString(getColumnIndexOrThrow(Tables.Moive.COLUMN_3))
                val stars = getFloat(getColumnIndexOrThrow(Tables.Moive.COLUMN_4))

                Moives.add(MovieModel(title,des,img,stars))
            }
        }

        Log.d("moives", Moives.toString())
        db.close()
        return Moives
    }

    fun isMovieCreated(title: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT " + Tables.Moive.COLUMN_1 + " FROM " + Tables.Moive.TABLE_MOIVE
                + " WHERE " + Tables.Moive.COLUMN_1 + " = '" + title +"'", null)
        return cursor.count
    }
    // functions like or dislike

    fun getmovielike(pos:Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM  " + Tables.Moive.TABLE_MOIVE + " WHERE " +  BaseColumns._ID + " =' " + pos +"'", null)
        cursor?.moveToFirst()
        val LikeIndicate = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.Moive.COLUMN_5))

        db.close()

        return LikeIndicate

    }
    fun getStars(pos:Int): Float {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM  " + Tables.Moive.TABLE_MOIVE + " WHERE " +  BaseColumns._ID + " =' " + pos +"'", null)
        cursor?.moveToFirst()
        val StarsIndicate = cursor.getFloat(cursor.getColumnIndexOrThrow(Tables.Moive.COLUMN_4))


        db.close()

        return StarsIndicate

    }
    fun incrementStars(Stars:Float, pos: Int){
        val db= writableDatabase
        val values = ContentValues().apply {
            put(Tables.Moive.COLUMN_4, Stars + 0.5)
        }

        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(pos.toString())
        db?.update(Tables.Moive.TABLE_MOIVE, values, selection,
                selectionArgs)
        db.close()
    }
    fun decrementStars(Stars:Float, pos: Int) {
        val db= writableDatabase
        val values = ContentValues().apply {
            put(Tables.Moive.COLUMN_4, Stars - 0.5)
        }

        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(pos.toString())
        db?.update(Tables.Moive.TABLE_MOIVE, values, selection,
                selectionArgs)
        db.close()
    }
    fun like(like:Int, pos:Int){
        val db = writableDatabase


        val values = ContentValues().apply {
            put(Tables.Moive.COLUMN_5, like +1)
        }

        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(pos.toString())
        db?.update(Tables.Moive.TABLE_MOIVE, values, selection,
                selectionArgs)
        db.close()
    }

    fun dislike(like:Int, pos:Int){
        val db = writableDatabase


        val values = ContentValues().apply {
            put(Tables.Moive.COLUMN_5, like -1)
        }

        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(pos.toString())
        db?.update(Tables.Moive.TABLE_MOIVE, values, selection,
                selectionArgs)
        db.close()
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MovieRs.db"


        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${Tables.User.TABLE_USER} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${Tables.User.COLUMN_1} TEXT NOT NULL UNIQUE," +
                        "${Tables.User.COLUMN_2} TEXT NOT NULL)"


        private const val SQL_CREATE_ENTRIES2 =
                "CREATE TABLE ${Tables.Moive.TABLE_MOIVE} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "${Tables.Moive.COLUMN_1} TEXT NOT NULL UNIQUE," +
                        "${Tables.Moive.COLUMN_2} TEXT NOT NULL,"+
                        "${Tables.Moive.COLUMN_3} TEXT NOT NULL DEFAULT 'no',"+
                        "${Tables.Moive.COLUMN_4}  REAL NOT NULL DEFAULT '0.0' ," +
                        "${Tables.Moive.COLUMN_5}  INT NOT NULL DEFAULT '0' )"


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