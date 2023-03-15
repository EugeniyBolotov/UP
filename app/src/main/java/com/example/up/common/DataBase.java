package com.example.up.common;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DataBase {
    public static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, "dataBase", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL("CREATE TABLE db_dish_version (id integer primary key autoincrement, version text);");
            database.execSQL("CREATE TABLE db_dish (id integer primary key autoincrement, " +
                    "dishId integer," +
                    "category text," +
                    "nameDish text," +
                    "price ineger," +
                    "icon text," +
                    "version text);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        }
    }
}
