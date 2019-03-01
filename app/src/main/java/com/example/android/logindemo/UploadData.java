package com.example.android.logindemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UploadData extends AppCompatActivity {

    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;
    private ImageView userProfilePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ImageView btnSpeak;
    protected static final int RESULT_SPEECH = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        userProfilePic = (ImageView)findViewById(R.id.ivProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

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
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                userProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                    if(text.get(0).equals("संकटकालीन मदत"))
                    {
                        startActivity(new Intent(UploadData.this, Emergancy.class));

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
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


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
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


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
                        startActivity(new Intent(UploadData.this, OrganDonation.class));

                    }
                    else if(text.get(0).equals("अवयवदान म्हणजे काय") || text.get(0).equals("अवयव दान म्हणजे काय"))
                    {
                        startActivity(new Intent(UploadData.this, WhatisOrganDonation.class));

                    }
                    else if(text.get(0).equals("अवयव दान कोण करू शकेल") || text.get(0).equals("अवयवदान कोण करू शकेल"))
                    {
                        startActivity(new Intent(UploadData.this, WhocanDo.class));

                    }
                    else if(text.get(0).equals("कोणते अवयव दान करू शकतो") || text.get(0).equals("कोणते अवयवदान करू शकतो"))
                    {
                        startActivity(new Intent(UploadData.this, WhichOrgans.class));

                    }
                    else if(text.get(0).equals("अवयवदान कसे करावे") || text.get(0).equals("अवयव दान कसे करावे"))
                    {
                        startActivity(new Intent(UploadData.this, HowtoDonate.class));

                    }
                    else if(text.get(0).equals("अवयवदान अर्ज भरा") || text.get(0).equals("अवयव दान अर्ज भरा"))
                    {
                        startActivity(new Intent(UploadData.this, Donation.class));

                    }
                    else if(text.get(0).equals("जन औषधालय") || text.get(0).equals("औषधालय") || text.get(0).equals("औषधालय शोधा") || text.get(0).equals("जन औषधालय शोधा") )
                    {
                        startActivity(new Intent(UploadData.this, MedicalStores.class));

                    }
                    else if(text.get(0).equals("सरकारी योजना") || text.get(0).equals("सरकारी योजना शोधा") )
                    {
                        startActivity(new Intent(UploadData.this, SchemesSelection.class));

                    }
                    else if(text.get(0).equals("फाइल संचयन उघडा") || text.get(0).equals("पर्याय उघडा प्रतिमा अपलोड करण्यासाठी") || text.get(0).equals("प्रतिमा अपलोड करण्यासाठी पर्याय उघडा") || text.get(0).equals("अपलोड"))
                    {
                        Intent intent = new Intent();
                        intent.setType("images/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);

                    }
                    else if(text.get(0).equals("डेटा अपलोड करा"))
                    {
                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                        String date = df.format(Calendar.getInstance().getTime());
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid()).child("Prescriptions"+"/"+date);
                        myRef.setValue(date);

                        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Prescriptions").child(date);  //User id/Images/Profile Pic.jpg
                        UploadTask uploadTask = imageReference.putFile(imagePath);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadData.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Toast.makeText(UploadData.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        startActivity(new Intent(UploadData.this,SecondActivity.class));

                    }
                    else if(text.get(0).equals("स्वताची वैद्यकीय माहिती भरा"))
                    {
                        startActivity(new Intent(UploadData.this, UploadData.class));

                    }
                    else if(text.get(0).equals("वैद्यकीय नोंदी दाखवा"))
                    {
                        startActivity(new Intent(UploadData.this, SeeData.class));

                    }


                }
                break;
            }

        }
    }

    public void submit(View view)
    {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid()).child("Prescriptions"+"/"+date);
        myRef.setValue(date);

        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Prescriptions").child(date);  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadData.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(UploadData.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(UploadData.this,SecondActivity.class));

    }


}
