package edu.uncc.inclass07;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class NoteDAO {
    private SQLiteDatabase db;

    public NoteDAO(SQLiteDatabase db){
        this.db = db;
    }

    public long save(Note note){
        ContentValues values = new ContentValues();
        values.put(NotesTable.columnSubj, note.getSubject());
        values.put(NotesTable.columnPriority, note.getPriority());
        values.put(NotesTable.columnComp, ""+note.isCompleted());
        return db.insert(NotesTable.TableName, null, values);

    }

    public boolean update(Note note){
        ContentValues values = new ContentValues();
        values.put(NotesTable.columnSubj, note.getSubject());
        values.put(NotesTable.columnPriority, note.getPriority());
        values.put(NotesTable.columnComp, ""+note.isCompleted());
        return db.update(NotesTable.TableName, values, NotesTable.columnID + "=?", new String[] {(note.getId()+"")}) >0;
    }

    public boolean delete(Note note){
        return db.delete(NotesTable.TableName, NotesTable.columnID + "=?",new String[] { note.getId()+""})>0;
    }

    public Note get(long id){
        Note note = null;

        Cursor c = db.query(true, NotesTable.TableName, new String[]{
                        NotesTable.columnID, NotesTable.columnSubj,
                        NotesTable.columnPriority, NotesTable.columnDate,
                        NotesTable.columnComp }, NotesTable.columnID+ "=?",
                new String[] {id + ""},null,null,null,null,null);

        if (c != null && c.moveToFirst()){
            note = buildNoteFromCursor(c);
            if (!c.isClosed()){
                c.close();
            }
        }
        return note;
    }


    public ArrayList<Note> getAll(){
        ArrayList<Note> notes = new ArrayList<Note>();
        Cursor c =db.query(NotesTable.TableName, new String[]{
                NotesTable.columnID, NotesTable.columnSubj,
                NotesTable.columnPriority, NotesTable.columnDate,
                NotesTable.columnComp }, null, null, null, null, null);


        if (c != null && c.moveToFirst()) {
            do {
                Note note = buildNoteFromCursor(c);
                if (note != null) {
                    notes.add(note);
                }
            } while (c.moveToNext());


            if (!c.isClosed()) {
                c.close();
            }
        }
        return notes;

    }

    private Note buildNoteFromCursor(Cursor c){
        Note note = null;
        if (c != null){
            note = new Note();
            note.setId(c.getLong(0));
            note.setSubject(c.getString(1));
            note.setPriority(c.getString(2));
            String date1 = c.getString(3);
            if("true".equalsIgnoreCase(c.getString(4))){
                note.setCompleted(true);
            }else{
                note.setCompleted(false);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat sdf1 = new SimpleDateFormat();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(date1);
                date = sdf2.parse(sdf1.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
            note.setCreatedOn(date);
        }
        return note;

    }
}
