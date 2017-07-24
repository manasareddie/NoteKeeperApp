package edu.uncc.inclass07;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseOpenHelper extends SQLiteOpenHelper {
    static final String dbName = "notesData.db";
    static final int DB_Version = 2;

    public DatabaseOpenHelper(Context context) {
        super(context, dbName, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        NotesTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        NotesTable.onUpgrade(sqLiteDatabase, i, i1);
    }
}
