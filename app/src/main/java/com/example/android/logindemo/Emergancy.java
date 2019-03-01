package com.example.android.logindemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Emergancy extends AppCompatActivity {
    private ImageView btnSpeak;
    protected static final int RESULT_SPEECH = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergancy);
        btnSpeak =(ImageView) findViewById(R.id.btnSpeak);


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


                    if(text.get(0).equals("संकटकालीन मदत"))
                    {
                        startActivity(new Intent(Emergancy.this, Emergancy.class));

                    }
                    else if(text.get(0).equals("पोलिसांना कॉल करा"))
                    {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:100"));
                        startActivity(intent);
                    }
                    else if(text.get(0).equals("रुग्णवाहिकेला कॉल करा") || text.get(0).equals("रुग्णवाहिके ला कॉल करा"))
                    {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:108"));
                        startActivity(intent);
                    }
                    else if(text.get(0).equals("मित्राला कॉल करा"))
                    {
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseDatabase = FirebaseDatabase.getInstance();


                        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid()+"/EmergancyHelp/Friend_Number");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String no=(String) dataSnapshot.getValue();
                                Intent intent = new Intent(Intent.ACTION_DIAL);

                                intent.setData(Uri.parse("tel:"+no));
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else if(text.get(0).equals("मित्राला संदेश पाठवा"))
                    {

                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseDatabase = FirebaseDatabase.getInstance();


                        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid()+"/EmergancyHelp");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String no=(String) dataSnapshot.child("Friend_Number").getValue();
                                String mess=(String)dataSnapshot.child("Emergancy_Message").getValue();


                                Uri uri = Uri.parse("smsto:"+no);
                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                intent.putExtra("sms_body", ""+mess);
                                startActivity(intent);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else if(text.get(0).equals("अवयवदान"))
                    {
                        startActivity(new Intent(Emergancy.this, OrganDonation.class));

                    }
                    else if(text.get(0).equals("अवयवदान म्हणजे काय") || text.get(0).equals("अवयव दान म्हणजे काय"))
                    {
                        startActivity(new Intent(Emergancy.this, WhatisOrganDonation.class));

                    }
                    else if(text.get(0).equals("अवयव दान कोण करू शकेल") || text.get(0).equals("अवयवदान कोण करू शकेल"))
                    {
                        startActivity(new Intent(Emergancy.this, WhocanDo.class));

                    }
                    else if(text.get(0).equals("कोणते अवयव दान करू शकतो") || text.get(0).equals("कोणते अवयवदान करू शकतो"))
                    {
                        startActivity(new Intent(Emergancy.this, WhichOrgans.class));

                    }
                    else if(text.get(0).equals("अवयवदान कसे करावे") || text.get(0).equals("अवयव दान कसे करावे"))
                    {
                        startActivity(new Intent(Emergancy.this, HowtoDonate.class));

                    }
                    else if(text.get(0).equals("अवयवदान अर्ज भरा") || text.get(0).equals("अवयव दान अर्ज भरा"))
                    {
                        startActivity(new Intent(Emergancy.this, Donation.class));

                    }
                    else if(text.get(0).equals("जन औषधालय") || text.get(0).equals("औषधालय") || text.get(0).equals("औषधालय शोधा") || text.get(0).equals("जन औषधालय शोधा") )
                    {
                        startActivity(new Intent(Emergancy.this, MedicalStores.class));

                    }
                    else if(text.get(0).equals("सरकारी योजना") || text.get(0).equals("सरकारी योजना शोधा") )
                    {
                        startActivity(new Intent(Emergancy.this, SchemesSelection.class));

                    }
                    else if(text.get(0).equals("स्वताची वैद्यकीय माहिती भरा"))
                    {
                        startActivity(new Intent(Emergancy.this, UploadData.class));

                    }

                    else if(text.get(0).equals("वैद्यकीय नोंदी दाखवा"))
                    {
                        startActivity(new Intent(Emergancy.this, SeeData.class));

                    }
                    else if(text.get(0).equals("माहिती शोधा"))
                    {
                        startActivity(new Intent(Emergancy.this, InformationActivity.class));

                    }
                    else  if (text.get(0).equals("नेमणूक बुक करा")) {
                        startActivity(new Intent(Emergancy.this, BookApointment.class));

                    }
                    else if (text.get(0).equals("नेमणूक रद्द करा")) {
                        startActivity(new Intent(Emergancy.this,CancelAppointment.class));

                    }
                }
                break;
            }

        }
    }

    public void callpolice(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:100"));
        startActivity(intent);
    }

    public void callambulance(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:108"));
        startActivity(intent);
    }

    public void callfriend(View view)
    {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid()+"/EmergancyHelp/Friend_Number");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                   String no=(String) dataSnapshot.getValue();
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:"+no));
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.addfriend:
                startActivity(new Intent(Emergancy.this, AddFriend.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
