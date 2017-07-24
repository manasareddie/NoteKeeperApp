package edu.uncc.inclass07;

import android.database.sqlite.SQLiteDatabase;

public class NotesTable {
    static final String TableName="notes";
    static final String columnID="id";
    static final String columnSubj="subject";
    static final String columnPriority="priority";
    static final String columnComp = "complete";
    static final String columnDate="createdOn";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+TableName+" (");
        sb.append(" "+columnID+" integer primary key autoincrement,");
        sb.append(" "+columnSubj+" text not null,");
        sb.append(" "+columnPriority+" text not null,");
        sb.append(" "+columnComp+" text not null,");
        sb.append(" "+columnDate+" datetime default current_timestamp   );");

        String sql = sb.toString();
        try {
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase db, int old, int newV){
        db.execSQL("DROP TABLE IF EXISTS "+TableName);
        NotesTable.onCreate(db);
    }

}
