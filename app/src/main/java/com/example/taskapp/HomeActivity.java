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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taskapp.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;
    // fire base
    //private FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("O&S Task App");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String user_id = mUser.getUid();

        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        mDataBase = dataBase.getReference().child("TaskNote").child(user_id);

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
                        String id = mDataBase.push().getKey();
                        String m_date = DateFormat.getDateInstance().format(new Date());
                        Data data = new Data(m_title, m_note, id, m_date,0,0);

                        mDataBase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

}