package com.example.taskapp;
//Omri and Shalev task app
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;


import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;
    // fire base
    private FirebaseDatabase dataBase;
    private DatabaseReference myRef;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private Query query;

    //recycler
    private RecyclerView recyclerView;

    private EditText titleUpd;
    private EditText noteUpd;
    private EditText urgUpd;
    private Button btnDel;
    private Button btnUdp;

    private String title;
    private String note;
    private String urg;
    private String post_key;



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

        dataBase = FirebaseDatabase.getInstance();
        //myRef = dataBase.getReference("https://task-app-9dfdd-default-rtdb.firebaseio.com/");
        mDataBase = dataBase.getReference().child("TaskNote").child(user_id);
        mDataBase.keepSynced(true);

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("TaskNote").child(user_id)
                .limitToLast(50);

        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


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
                RadioGroup radioGroup = my_view.findViewById(R.id.radiogroup);

                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String m_title = title.getText().toString().trim();
                        String m_note = note.getText().toString().trim();
                        String urgency = "low";
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        if (selectedId != -1) {
                            RadioButton radioButton = (RadioButton) my_view.findViewById(selectedId);
                             urgency = radioButton.getText().toString();
                        }
                        else
                        {
                            RadioButton rb = (RadioButton) my_view.findViewById(R.id.high);
                            rb.setError("Required Filed");
                            return;
                        }
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
                        Data data = new Data(m_title, m_note, m_date, id, urgency);

                        mDataBase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }


    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(query,Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        options
                )
        {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_data, parent, false);

                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {
                holder.setTitle(model.getTitle());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
                holder.setUrgency(model.getUrgency());

                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        title = model.getTitle();
                        note = model.getNote();
                        urg = model.getUrgency();

                        updateData();
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            myView = itemView;
        }

        public void setTitle(String title)
        {
            TextView mTitle = myView.findViewById(R.id.item_title);
            mTitle.setText(title);
        }
        public void setNote(String note)
        {
            TextView mNote = myView.findViewById(R.id.item_note);
            mNote.setText(note);
        }

        public void setDate(String date)
        {
            TextView mDate = myView.findViewById(R.id.date);
            mDate.setText(date);
        }

        public void setUrgency(String urgency)
        {
            TextView mUrg = myView.findViewById(R.id.urgency_item);

            mUrg.setText(urgency);
            if (urgency.contains("high"))
            {
                mUrg.setTextColor(Color.RED);
            }
            else if(urgency.contains("medium"))
            {
                //yellow
                mUrg.setTextColor(Color.parseColor("#FFBF00"));
            }
            else if(urgency.contains("low"))
            {
                //green
                mUrg.setTextColor(Color.parseColor("#57CE5C"));
            }
        }

    }

   public void updateData(){
       AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
       LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

       View myView = inflater.inflate(R.layout.updateinput,null);

       myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();

       titleUpd = myView.findViewById(R.id.upd_title);
       noteUpd = myView.findViewById(R.id.upd_note);
       titleUpd.setText(title);
       titleUpd.setSelection(title.length());

       noteUpd.setText(note);
       noteUpd.setSelection(note.length());

       btnDel = myView.findViewById(R.id.del_btn);
       btnUdp = myView.findViewById(R.id.upd_btn);
       RadioGroup radioGroupUpd = myView.findViewById(R.id.upd_radiogroup);
       if (urg.contains("high"))
           radioGroupUpd.check(R.id.upd_high);
       else if (urg.contains("medium"))
           radioGroupUpd.check(R.id.upd_medium);
       else if (urg.contains("high"))
           radioGroupUpd.check(R.id.upd_low);


       btnUdp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               title = titleUpd.getText().toString().trim();
               note = noteUpd.getText().toString().trim();

               int selectedId = radioGroupUpd.getCheckedRadioButtonId();

               if (selectedId != -1) {
                   RadioButton radioButton = (RadioButton) myView.findViewById(selectedId);
                   urg = radioButton.getText().toString();
               }
               else
               {
                   RadioButton rb = (RadioButton) myView.findViewById(R.id.upd_high);
                   rb.setError("Required Filed");
                   return;
               }
               if(TextUtils.isEmpty(title))
               {
                   titleUpd.setError("Required Filed");
                   return;
               }
               if(TextUtils.isEmpty(note))
               {
                   noteUpd.setError("Required Filed");
                   return;
               }


               String id = mDataBase.push().getKey();
               String m_date = DateFormat.getDateInstance().format(new Date());
               Data data = new Data(title, note, m_date, id, urg);

               mDataBase.child(post_key).setValue(data);

               dialog.dismiss();
           }
       });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}