package com.findme.msv.findme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Main2Activity extends AppCompatActivity {


    String[] departments={"CSE","IT","ECE","MECH","EEE","BME","CIVIL","CHEM"};
    String[] year={"1","2","3","4"};
    String[] section={"A","B"};
    String name;
    Spinner sp1,sp2;
    EditText et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        et=(EditText) findViewById(R.id.editText2);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,departments);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,year);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,section);


        sp1 = (Spinner) findViewById(R.id.departments);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);


        sp2 = (Spinner) findViewById(R.id.year);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);





    }

    public void forMaps(View view)
    {
        //Toast.makeText(getBaseContext(),"Before bundle:"+et.getText().toString(),Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(Main2Activity.this, MapsActivity.class);
        Bundle b=new Bundle();
        b.putString("name",et.getText().toString());
        b.putString("dep",sp1.getSelectedItem().toString());
        b.putString("year",sp2.getSelectedItem().toString());
        b.putString("key","0");
        intent.putExtras(b);
        startActivity(intent);
    }
}