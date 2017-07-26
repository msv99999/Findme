package com.findme.msv.findme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;


public class Main2Activity extends AppCompatActivity {


    String[] departments={"CSE","IT","ECE","MECH","EEE","BME","CIVIL","CHEMICAL"};
    String[] year={"1","2","3","4"};
    String[] section={"A","B"
    };







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,departments);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,year);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,section);


        Spinner sp1 = (Spinner) findViewById(R.id.departments);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);


        Spinner sp2 = (Spinner) findViewById(R.id.year);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        Spinner sp3 = (Spinner) findViewById(R.id.section);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(adapter3);



    }

    public void forMaps(View view)
    {
        Intent intent = new Intent(Main2Activity.this, MapsActivity.class);
        startActivity(intent);
    }
}
