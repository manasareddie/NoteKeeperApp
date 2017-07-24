package edu.uncc.inclass07;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> note_list = new ArrayList<Note>();
    DatabaseManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbmanager = new DatabaseManager(this);
        String[] spArr = getResources().getStringArray(R.array.dropdown);
        ArrayAdapter<String> spAdap= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spArr);
        Spinner sp = (Spinner)findViewById(R.id.spinner);
        sp.setAdapter(spAdap);
        note_list = dbmanager.getAllNotes();
        setListView(note_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInf = getMenuInflater();
        mInf.inflate(R.menu.action_menu,menu);
        return true;
    }

    public void addNote(View view){
        EditText ed = (EditText)findViewById(R.id.nameOfNote);
        String text = ed.getText().toString();
        if("".equals(text)){
            Toast.makeText(this, "Please enter some text to create a note!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Note n = new Note();
            n.setSubject(text);
            Spinner spinner = (Spinner)findViewById(R.id.spinner);
            String spText = spinner.getSelectedItem().toString();
            n.setPriority(spText+" Priority");
            dbmanager.saveNote(n);
            Log.d("demo","Note Saved!");
            note_list = dbmanager.getAllNotes();
            setListView(note_list);

        }
    }

    public void setListView(ArrayList<Note> appList){
        final ListView lv = (ListView)findViewById(R.id.listView1);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note note = (Note)lv.getItemAtPosition(i);
                if(note!=null){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete")
                            .setMessage("Do you really want to delete this note?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dbmanager.deleteNote(note);
                                    note_list = dbmanager.getAllNotes();
                                    setListView(note_list);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                return true;
            }
        });
        NoteAdapter adapter = new NoteAdapter(this, R.layout.text_checkbox, appList);
        lv.setAdapter(adapter);
        lv.setLongClickable(true);
        lv.setClickable(true);
        adapter.setNotifyOnChange(true);


    }

    public boolean onOptionsItemSelected(MenuItem item) {

        boolean status = true;
        switch (item.getItemId()) {
            case R.id.showAll:
                note_list = dbmanager.getAllNotes();
                setListView(note_list);
                break;
            case R.id.showComp:
                note_list = dbmanager.getAllNotes();
                ArrayList<Note> nList = new ArrayList<Note>();
                Iterator<Note> it = note_list.iterator();
                while(it.hasNext()){
                    Note n =it.next();
                    if(n.isCompleted()){
                        nList.add(n);
                    }
                }
                setListView(nList);
                break;
            case R.id.showPen:
                note_list = dbmanager.getAllNotes();
                nList = new ArrayList<Note>();
                it = note_list.iterator();
                while(it.hasNext()){
                    Note n =it.next();
                    if(!n.isCompleted()){
                        nList.add(n);
                    }
                }
                setListView(nList);
                break;
            case R.id.sortTime:
                note_list = dbmanager.getAllNotes();
                Collections.sort(note_list, new TimeComparator());
                setListView(note_list);
                break;
            case R.id.sortPri:
                note_list = dbmanager.getAllNotes();
                Collections.sort(note_list, new PriorComparator());
                setListView(note_list);
                break;
        }
        return true;
    }

    class PriorComparator implements Comparator<Note>{
        @Override
        public int compare(Note app1, Note app2) {
            String prc1 = app1.getPriority();
            int num = priornum(prc1);
            String prc2 = app2.getPriority();
            int num1 = priornum(prc2);
            return num1-num;
        }

        public int priornum(String text){
            int num = 0;
            switch (text){
                case "High Priority":
                    num = 3;
                    break;
                case "Medium Priority":
                    num = 2;
                    break;
                case "Low Priority":
                    num = 1;
                    break;
            }
            return num;
        }
    }

    class TimeComparator implements Comparator<Note>{
        @Override
        public int compare(Note app1, Note app2) {
            Date prc1 = app1.getCreatedOn();
            long num = prc1.getTime();
            Date prc2 = app2.getCreatedOn();
            long num1 = prc2.getTime();
            return (int)(num1-num);
        }
    }
}
