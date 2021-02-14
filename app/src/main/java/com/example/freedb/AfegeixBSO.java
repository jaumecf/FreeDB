package com.example.freedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;


public class AfegeixBSO extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegeix_b_s_o);


    }
    public void showTimePickerDialog(View v) {
        SelectorTemps newFragment = new SelectorTemps();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}