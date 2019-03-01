package com.example.android.logindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OfferInside extends AppCompatActivity {

    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_inside);
        t1=(TextView)findViewById(R.id.text1);
        t1.setText("Schemes for hospitals and medicals near:" +StringPasser2.stroreid);
    }
}
