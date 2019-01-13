package com.example.yash.proxymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Write_note extends AppCompatActivity {

    EditText editnote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        editnote = (EditText) findViewById(R.id.note);
        Intent intent = getIntent();
        final int noteId = intent.getIntExtra("noteId",-1);
        final SharedPreferences sp = this.getSharedPreferences("com.example.yash.notes",MODE_PRIVATE);

        if(noteId != -1) {
            String message = Notes.notes.get(noteId);
            editnote.setText(message);

            if(noteId == 0){
                editnote.setText("write here");
            }

            editnote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Notes.notes.set(noteId,String.valueOf(charSequence));
                    Notes.note_adapter.notifyDataSetChanged();
                    try {
                        sp.edit().putString("notes",ObjectSerializer.serialize(Notes.notes)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

}
