package com.example.android.logindemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class DoctorCompleteInfo extends AppCompatActivity {

    private DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;
    Vector<String> v = new Vector<String>();
    ListView listView;
    private ImageView btnSpeak;
    protected static final int RESULT_SPEECH = 1;


    TextView t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_complete_info);

        t1 = (TextView) findViewById(R.id.name);
        t2 = (TextView) findViewById(R.id.address);
        t3 = (TextView) findViewById(R.id.phone);

         firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor/" + StringPasser3.storeid + "/" + StringPasser.storeid + "/address");

        t1.setText("नाव :" + StringPasser.storeid);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                String d = (String) dataSnapshot.getValue();

                t2.setText("पूर्ण पत्ता :" + d);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor/" + StringPasser3.storeid + "/" + StringPasser.storeid + "/phonenumber");


        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                String d = (String) dataSnapshot.getValue();

                t3.setText("फोन नंबर :" + d);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "mr-IN");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);

                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                    if (text.get(0).equals("बुक") || text.get(0).equals("बुक करा") ) {
                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                        String date = df.format(Calendar.getInstance().getTime());

                        FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getUid()+"/Appointments").child(date).setValue(StringPasser.storeid);

                        Toast toast = Toast.makeText(getApplicationContext(), "Added Appointment Successfully !!", Toast.LENGTH_SHORT); toast.show();
                        startActivity(new Intent(DoctorCompleteInfo.this, SecondActivity.class));

                    }
                }

            }

        }
    }


    public void updateitit(View view)
    {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getUid()+"/Appointments").child(date);
        firebaseDatabase.setValue(StringPasser.storeid);

        Toast toast = Toast.makeText(getApplicationContext(), "Added Appointment Successfully !!", Toast.LENGTH_SHORT); toast.show();
        startActivity(new Intent(DoctorCompleteInfo.this, SecondActivity.class));

    }

}
