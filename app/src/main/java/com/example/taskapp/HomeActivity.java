package com.example.taskapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("O&S Task App");

        fabBtn = findViewById(R.id.fab_btn);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder my_dialog = new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

                View my_view = inflater.inflate(R.layout.custom_input, null);

                my_dialog.setView(my_view);

                AlertDialog dialog = my_dialog.create();

                EditText title = my_view.findViewById(R.id.edt_title);
                EditText note = my_view.findViewById(R.id.edt_note);
                Button save_btn = my_view.findViewById(R.id.save_btn);
                RadioGroup rg = my_view.findViewById(R.id.radiogroup);


                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String m_title = title.getText().toString().trim();
                        String m_note = note.getText().toString().trim();
                        rg.getCheckedRadioButtonId();

                        if(TextUtils.isEmpty(m_title))
                        {
                            title.setError("Required Filed");
                            return;
                        }
                        if(TextUtils.isEmpty(m_note))
                        {
                            note.setError("Required Filed");
                            return;
                        }
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup,
                                                         int radioButtonID) {
                                switch(radioButtonID) {
                                    case R.id.low:
                                        listItem.setAnswerID(1);
                                        break;
                                    case R.id.medium:
                                        listItem.setAnswerID(2);
                                        break;
                                    case R.id.high:
                                        listItem.setAnswerID(2);
                                        break;
                                }
                            }
                        });


                    }
                });
                dialog.show();
            }
        });

    }

}