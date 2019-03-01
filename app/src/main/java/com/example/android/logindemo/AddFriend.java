package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriend extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void submitit(View view)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        String no=((EditText)findViewById(R.id.number)).getText().toString();
        String mess=((EditText)findViewById(R.id.message)).getText().toString();

        myRef.child("EmergancyHelp/Friend_Number").setValue(no);
        myRef.child("EmergancyHelp/Emergancy_Message").setValue(mess);
        Toast toast = Toast.makeText(getApplicationContext(), "Updated Successfully !", Toast.LENGTH_SHORT);
        toast.show();

    }
}
