package com.example.android.logindemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ImageView btnSpeak;
    protected static final int RESULT_SPEECH = 1;
    private FirebaseDatabase firebaseDatabase;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private ImageView imf;
    private TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth = FirebaseAuth.getInstance();


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



        dl=(DrawerLayout)findViewById(R.id.draw);
        abdt=new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        NavigationView nav_view=(NavigationView)findViewById(R.id.nav_view);
        imf=(ImageView)nav_view.findViewById(R.id.profilepicofbar);
        t1=(TextView)nav_view.findViewById(R.id.emaiidofbar);


        setthebar();


        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();

                if(id==R.id.id1)
                {
                    Toast.makeText(SecondActivity.this,"My Profile",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SecondActivity.this, ProfileActivity.class));

                }
                else if(id==R.id.id2)
                {

                }
                else if(id==R.id.id3)
                {

                }
                else if(id==R.id.id4)
                {

                }
                else if(id==R.id.id5)
                {

                }
                else if(id==R.id.id6)
                {

                }
                else if(id==R.id.id7)
                {

                }
                else if(id==R.id.id8)
                {

                }

                return true;
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
                        startActivity(new Intent(SecondActivity.this, Emergancy.class));

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
                        startActivity(new Intent(SecondActivity.this, OrganDonation.class));

                    }
                    else if(text.get(0).equals("अवयवदान म्हणजे काय") || text.get(0).equals("अवयव दान म्हणजे काय"))
                    {
                        startActivity(new Intent(SecondActivity.this, WhatisOrganDonation.class));

                    }
                    else if(text.get(0).equals("अवयव दान कोण करू शकेल") || text.get(0).equals("अवयवदान कोण करू शकेल"))
                    {
                        startActivity(new Intent(SecondActivity.this, WhocanDo.class));

                    }
                    else if(text.get(0).equals("कोणते अवयव दान करू शकतो") || text.get(0).equals("कोणते अवयवदान करू शकतो"))
                    {
                        startActivity(new Intent(SecondActivity.this, WhichOrgans.class));

                    }
                    else if(text.get(0).equals("अवयवदान कसे करावे") || text.get(0).equals("अवयव दान कसे करावे"))
                    {
                        startActivity(new Intent(SecondActivity.this, HowtoDonate.class));

                    }
                    else if(text.get(0).equals("अवयवदान अर्ज भरा") || text.get(0).equals("अवयव दान अर्ज भरा"))
                    {
                        startActivity(new Intent(SecondActivity.this, Donation.class));

                    }
                    else if(text.get(0).equals("जन औषधालय") || text.get(0).equals("औषधालय") || text.get(0).equals("औषधालय शोधा") || text.get(0).equals("जन औषधालय शोधा") )
                    {
                        startActivity(new Intent(SecondActivity.this, MedicalStores.class));

                    }
                    else if(text.get(0).equals("सरकारी योजना") || text.get(0).equals("सरकारी योजना शोधा") )
                    {
                        startActivity(new Intent(SecondActivity.this, SchemesSelection.class));

                    }
                    else if(text.get(0).equals("स्वताची वैद्यकीय माहिती भरा") || text.get(0).equals("वैद्यकीय माहिती भरा"))
                    {
                        startActivity(new Intent(SecondActivity.this, UploadData.class));

                    }
                    else if(text.get(0).equals("वैद्यकीय नोंदी दाखवा") || text.get(0).equals("स्वताची वैद्यकीय नोंदी दाखवा") ||  text.get(0).equals("वैद्यकीय नोंदी"))
                    {
                        startActivity(new Intent(SecondActivity.this, SeeData.class));

                    }
                    else if(text.get(0).equals("सवलती व योजना") || text.get(0).equals("सवलती") || text.get(0).equals("योजना दाखवा")  || text.get(0).equals("सवलती व योजना दाखवा") || text.get(0).equals("सवलती दाखवा") || text.get(0).equals("योजना")   )
                    {
                        startActivity(new Intent(SecondActivity.this, OffersFirst.class));

                    }
                    else if(text.get(0).equals("माहिती शोधा"))
                    {
                        startActivity(new Intent(SecondActivity.this, InformationActivity.class));

                    }
                    else  if (text.get(0).equals("नेमणूक बुक करा") || text.get(0).equals("अपॉईंटमेंट बुक करा") || text.get(0).equals("अपॉईंटमेंट बुक कर")) {
                        startActivity(new Intent(SecondActivity.this, BookApointment.class));

                    }
                    else if (text.get(0).equals("नेमणूक रद्द करा") ||text.get(0).equals("अपॉईंटमेंट रद्द करा") ||text.get(0).equals("अपॉईंटमेंट कॅन्सेल करा")) {
                        startActivity(new Intent(SecondActivity.this,CancelAppointment.class));

                    }
                    else if(text.get(0).equals("औषधांची आठवण") || text.get(0).equals("स्मरणपत्र जोडा") )
                    {
                        startActivity(new Intent(SecondActivity.this,OpenRemainder.class));

                    }
                }
                break;
            }

        }
    }
        private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.profileMenu:
                startActivity(new Intent(SecondActivity.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    public void helpp(View view)
    {
        startActivity(new Intent(SecondActivity.this, Emergancy.class));

    }
    public void opendonation(View view)
    {
        startActivity(new Intent(SecondActivity.this, OrganDonation.class));


    }

    public void openmedical(View view)
    {
        startActivity(new Intent(SecondActivity.this, MedicalStores.class));
    }

    public void openschemes(View view)
    {
        startActivity(new Intent(SecondActivity.this,SchemesSelection.class));
    }
    public void openuploaddata(View view)
    {
        startActivity(new Intent(SecondActivity.this,UploadData.class));
    }
    public void openseedata(View view)
    {
        startActivity(new Intent(SecondActivity.this,SeeData.class));
    }

    public void openseeoffers(View view)
    {
        startActivity(new Intent(SecondActivity.this,OffersFirst.class));

    }
    public void openinfo(View view)
    {
        startActivity(new Intent(SecondActivity.this,InformationActivity.class));

    }

    public void openremainder(View view)
    {
        startActivity(new Intent(SecondActivity.this,OpenRemainder.class));

    }

    public void setthebar()
    {


    }
}
