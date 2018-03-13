package com.example.subhamdivakar.alice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subhamdivakar.alice.UTILS.SqDB;

public class DisplaySavedContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_saved_contacts);

        SqDB obj = new SqDB(this);
        if(obj==null)
        {
            Toast.makeText(getApplicationContext(), "Contacts not stored in database.Please Store Contacts first.", Toast.LENGTH_LONG).show();
            Intent q=new Intent(this,Contacts.class);
            startActivity(q);
        }
        else {
            String p1 = String.valueOf(obj.getContact().phone1);
            String p2 = String.valueOf(obj.getContact().phone2);
            String p3 = String.valueOf(obj.getContact().phone2);
            String p4 = String.valueOf(obj.getContact().phone2);
            String p5 = String.valueOf(obj.getContact().phone2);
            TextView messageView2 = findViewById(R.id.textView3);
            messageView2.setText(p1);
            TextView messageView3 = findViewById(R.id.textView4);
            messageView3.setText(p2);
            TextView messageView4 = findViewById(R.id.textView6);
            messageView4.setText(p3);
            TextView messageView5 = findViewById(R.id.textView7);
            messageView5.setText(p4);
            TextView messageView6 = findViewById(R.id.textView8);
            messageView6.setText(p5);
        }
    }
}
