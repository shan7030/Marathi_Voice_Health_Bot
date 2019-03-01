package com.example.android.logindemo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UploadPrecription extends AppCompatActivity {

    private ImageView profilePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_precription);

        profilePic = findViewById(R.id.ivProfilePic);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        StorageReference storageReference = firebaseStorage.getReference();
        Log.v("UploadPrecription","okoko :: "+StringPasser.storeid);
        Log.v("UploadPrecription","okoko : "+firebaseAuth.getUid());
        storageReference.child(firebaseAuth.getUid()).child("Images/Prescriptions/"+StringPasser.storeid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilePic);
            }
        });


    }


}
