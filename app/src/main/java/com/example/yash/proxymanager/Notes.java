package com.example.yash.proxymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Notes extends AppCompatActivity {

    static ListView notelist ;
    static ArrayList<String> notes;
    static ArrayAdapter note_adapter;
    static  int  number_of_notes;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.add_note:
                notes.add("");
                Intent intent = new Intent(getApplicationContext(),Write_note.class);
                intent.putExtra("noteId",number_of_notes);
                startActivity(intent);
                number_of_notes++;
                final SharedPreferences sp = this.getSharedPreferences("com.example.yash.notes",MODE_PRIVATE);
                sp.edit().putInt("amount",number_of_notes).apply();
                note_adapter.notifyDataSetChanged();
                return true;
            default : return false;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notelist = (ListView) findViewById(R.id.notelist);
        notes = new ArrayList<String>();
        final SharedPreferences sp = this.getSharedPreferences("com.example.yash.notes",MODE_PRIVATE);

        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sp.getString("notes", ObjectSerializer.serialize(new ArrayList<String>())));
            number_of_notes = (int) sp.getInt("amount",0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        note_adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        notelist.setAdapter(note_adapter);

        if(number_of_notes == 0){
            notes.add("first");
            Intent intent = new Intent(getApplicationContext(),Write_note.class);
            intent.putExtra("noteId",number_of_notes);
            startActivity(intent);
            number_of_notes++;
            final SharedPreferences save = this.getSharedPreferences("com.example.yash.notes",MODE_PRIVATE);
            save.edit().putInt("amount",number_of_notes).apply();
            note_adapter.notifyDataSetChanged();
        }


        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Write_note.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });

        notelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;

                new AlertDialog.Builder(Notes.this)
                        .setTitle("Delete")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to delete this note.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(position);
                                note_adapter.notifyDataSetChanged();
                                try {
                                    sp.edit().putString("notes",ObjectSerializer.serialize(Notes.notes)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(Notes.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(Notes.this, "Note not deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

                return true;
            }
        });

    }
}
