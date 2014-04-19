
package net.tigerparents.nut;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by xiaoqin on 4/9/2014.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static String food_nutr_tab_name = "FOOD_NUT_DATA";
    public static String nutr_desc_tab_name = "NUTR_DEF";
    public static String daily_std_tab_name = "DAILY_STD_NUTR_TABLE";
    public static String person_profile_tab_name = "PERSON_PROFILE_TABLE";
    public static String daily_food_log = "DAILY_FOOD_LOG";
    public static String weekly_food_log = "WEEKLY_FOOD_LOG";
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/net.tigerparents.nut/databases/";
    private static String DB_NAME = "food.db";
    private final Context myContext;
    public SQLiteDatabase myDataBase = null;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public SQLiteDatabase getDataBase() {
        if (myDataBase == null)
            openDataBase(SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    public Context getContext() {
        return myContext;
    }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                InputStream myInput = myContext.getAssets().open(DB_NAME);

                // Path to the just created empty db
                String outFileName = DB_PATH + DB_NAME;
                //Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(outFileName);

                copyDataBase(myInput, myOutput);
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase(InputStream myInput, OutputStream myOutput) throws IOException {

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public void openDataBase(int mode) throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, mode);
        createAllTables();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public void copyLogs(String filename) {

        SQLiteDatabase inDatabase = SQLiteDatabase.openDatabase(filename, null, SQLiteDatabase.OPEN_READONLY);
        Cursor in_cursor = inDatabase.rawQuery("select * from " + daily_food_log + ";", null);

    }

    public void copyTable(String out_file_name, String table_name) {
        try {
            SQLiteDatabase inDatabase = SQLiteDatabase.openDatabase(out_file_name, null, SQLiteDatabase.OPEN_READONLY);
            Cursor out_cursor = inDatabase.rawQuery("select * from " + table_name + ";", null);
            if (out_cursor.moveToFirst()) {
                return;
            }

            do {
                ContentValues contentValues = new ContentValues();
                contentValues.put(" _date", out_cursor.getInt(0));
                contentValues.put("food_name" + "\'", out_cursor.getString(1));
                contentValues.put("weight", out_cursor.getDouble(2));
                getDataBase().insert(table_name, null, contentValues);
            } while (out_cursor.moveToNext());
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public boolean execSQL(String sql, String table_name) {
        /* give table name for create tables */
        try {
            getDataBase().beginTransaction();
            getDataBase().execSQL(sql);
            getDataBase().setTransactionSuccessful();
            getDataBase().endTransaction();
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e(e.getClass().getName(), e.getMessage(), e);
            return false;
        }

    }

    public void createAllTables() {
        String sql;
        /* create daily std nutrition table */
        String table_name = daily_std_tab_name;
        sql = "create table if not exists " + table_name + " ( " +
                "_status STRING , " +
                " age_group STRING, " +
                " \"291\" DOUBLE, " +
                " \"301\" DOUBLE, " +
                " \"205\" DOUBLE, " +
                " \"203\" DOUBLE, " +
                " \"320\" DOUBLE, " +
                " \"401\" DOUBLE, " +
                " \"328\" DOUBLE, " +
                " \"323\" DOUBLE, " +
                " \"404\" DOUBLE, " +
                " \"405\" DOUBLE, " +
                " \"406\" DOUBLE, " +
                " \"415\" DOUBLE, " +
                " \"417\" DOUBLE, " +
                " \"418\" DOUBLE, " +
                " \"312\" DOUBLE, " +
                " \"002\" DOUBLE, " +
                " \"303\" DOUBLE, " +
                " \"304\" DOUBLE, " +
                " \"003\" DOUBLE, " +
                " \"305\" DOUBLE, " +
                " \"317\" DOUBLE, " +
                " \"309\" DOUBLE " +
                ");";
        execSQL(sql, table_name);
        writeDailySTDTable(table_name);
        readTable(table_name);

         /* create person profile table */
        table_name = person_profile_tab_name;
        sql = "create table if not exists " + table_name + " (" +
                "_name STRING PRIMARY KEY, " +
                "birth INT, " +
                "gender STRING, " +
                "status STRING, " +
                "weight DOUBLE " +
                ")";
        execSQL(sql, table_name);

        /* create daily food log */
        table_name = daily_food_log;
        sql = "create table if not exists " + table_name + "  (" +
                "_date INT, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);

        /* create weekly food log */
        table_name = weekly_food_log;
        sql = "create table if not exists " + table_name + " (" +
                "_date INT, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);
    }

    public void writeDailySTDTable(String table_name) {

        try {
            InputStream input = myContext.getAssets().open("std_daily_table.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;

            getDataBase().beginTransaction();

            while ((line = br.readLine()) != null) {
                /* get database column names and count */
                ContentValues contentValues = new ContentValues();

                Cursor cursor = getDataBase().rawQuery("select * from " + table_name, null);
                String[] col_names = cursor.getColumnNames();

                String[] words = line.split("\t");

                if (words.length == 0) continue;
                for (int i = 0; i < words.length; i++) {
                    contentValues.put("\'" + col_names[i] + "\'", words[i]);
                }
                System.out.print("test");
                getDataBase().insert(table_name, null, contentValues);
            }
            getDataBase().setTransactionSuccessful();
            getDataBase().endTransaction();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public void getAllTables() {
        String SQL_GET_ALL_TABLES = "SELECT name FROM " +
                "sqlite_master WHERE type='table' ORDER BY name";
        Cursor cursor = getDataBase().rawQuery(SQL_GET_ALL_TABLES, null);
        System.out.print("test");
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String tablename = cursor.getString(0);
                System.out.println(tablename);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void readTable(String table_name) {
        String sql = "select * from " + table_name + ";";

        try {
            Cursor cursor = getDataBase().rawQuery(sql, null);

            int count = cursor.getCount();

            if (!cursor.moveToFirst()) {
                System.out.print(count);
            }

            while (!cursor.isAfterLast()) {
                String status = cursor.getString(0);
                String age_group = cursor.getString(1);
                double val1 = cursor.getDouble(2);
                double val13 = cursor.getDouble(13);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.v(e.getClass().getName(), e.getMessage(), e);
        }
    }
}
