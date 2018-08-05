package com.vinaygaba.creditcardview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vinaygaba.creditcardview.CreditCardView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreditCardView creditCardView = findViewById(R.id.card3);
        creditCardView.setCardName("Vinay Gaba");
    }

}
