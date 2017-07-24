package edu.uncc.inclass07;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import org.ocpsoft.prettytime.PrettyTime;
import java.util.ArrayList;


public class NoteAdapter extends ArrayAdapter {
    Context ctx = null;
    ArrayList<Note> noteList = null;
    int resource = 0;

    public NoteAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.noteList = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inf.inflate(resource, parent, false);

        final View conV = convertView;

        final Note n = noteList.get(position);
        TextView tv = (TextView)convertView.findViewById(R.id.noteSubj);
        tv.setText(n.getSubject());
        tv = (TextView)convertView.findViewById(R.id.notePrio);
        tv.setText(n.getPriority());

        PrettyTime p = new PrettyTime();
        Log.d("demo",""+n.getCreatedOn());
        String dateFormat = p.format(n.getCreatedOn());

        tv = (TextView)convertView.findViewById(R.id.noteTime);
        tv.setText(dateFormat);

        CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkBox);
        if(n.isCompleted()){
            cb.setChecked(true);
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox)view;

                if(n.isCompleted() && !(checkBox.isChecked())){
                    new AlertDialog.Builder(ctx)
                        .setTitle("Pending")
                        .setMessage("Do you really want to mark this as pending?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                n.setCompleted(false);
                                (new DatabaseManager(ctx)).updateNote(n);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }else if(!(n.isCompleted()) && (checkBox.isChecked())){
                    new AlertDialog.Builder(ctx)
                        .setTitle("Completed")
                        .setMessage("Do you really want to mark this as complete?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                n.setCompleted(true);
                                (new DatabaseManager(ctx)).updateNote(n);
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
            }
        });

        return convertView;
    }
}
