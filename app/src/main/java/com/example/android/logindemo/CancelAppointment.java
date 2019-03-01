package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class CancelAppointment extends AppCompatActivity {

    private DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;
    Vector<String> v=new Vector<String>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getUid()).child("Appointments");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    String date = uniqueKeySnapshot.getKey();
                    v.add(date);
                }
                listView = (ListView) findViewById(R.id.listofvisits);

                ArrayAdapter adapter = new ArrayAdapter<String>(CancelAppointment.this, R.layout.listofvisits, v);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selected = (String) adapterView.getItemAtPosition(i);
                        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("Appointments").child(selected).removeValue();

                         Intent intent = new Intent(CancelAppointment.this, SecondActivity.class);

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
