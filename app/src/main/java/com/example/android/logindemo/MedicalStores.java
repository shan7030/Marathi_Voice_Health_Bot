package com.example.android.logindemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MedicalStores extends AppCompatActivity {

    private ImageView btnSpeak;
    protected static final int RESULT_SPEECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_stores);
        (findViewById(R.id.mainid)).getBackground().setAlpha(50);
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


                    if (text.get(0).equals("पुणे")) {
                        startActivity(new Intent(MedicalStores.this, PuneMedical.class));

                    }
                    else if (text.get(0).equals("नाशिक")) {
                        startActivity(new Intent(MedicalStores.this, NashikMedical.class));

                    }
                    else if(text.get(0).equals("सरकारी योजना") || text.get(0).equals("सरकारी योजना शोधा") )
                    {
                        startActivity(new Intent(MedicalStores.this, SchemesSelection.class));

                    }
                }
            }
        }
    }
}
