package com.findme.msv.findme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

public class Main3Activity extends AppCompatActivity {

    String[] faculty_position={"Professor","Associate Professor","Assistant Professor","Clerk"};
    String[] departments={"CSE","IT","ECE","MECH","EEE","BME","CIVIL","CHEMICAL"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,departments);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,faculty_position);

        Spinner sp1 = (Spinner) findViewById(R.id.departments);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);


        Spinner sp2 = (Spinner) findViewById(R.id.faculty_position);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);

    }

    public void forMaps(View view)
    {
        Intent intent = new Intent(Main3Activity.this, MapsActivity.class);
        startActivity(intent);
    }
}
