package com.example.subhamdivakar.alice;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subhamdivakar.alice.Bean.ContactSaving;
import com.example.subhamdivakar.alice.UTILS.SqDB;

import java.util.List;

import static android.Manifest.permission.SEND_SMS;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p1;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p2;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p3;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p4;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p5;

public class Contacts extends AppCompatActivity {

    private static final int REQUEST_SMS = 0;
    private static final int REQ_PICK_CONTACT = 2;
    private EditText phoneEditText;
    private EditText messageEditText;
    boolean f1 = false, f2 = false, f3 = false, f4 = false, f5 = false;

    private ImageView pickContact, pickContact2, pickContact3, pickContact4, pickContact5;


    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
    Button button;
    TextView name, number1, number2, number3, number4, number5;
    public static final int RequestPermissionCode = 1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        SqDB database = new SqDB(this);
        ContactSaving obj = database.getContact();
        number1 = (TextView) findViewById(R.id.phone_number_edit_text);
        number2 = (TextView) findViewById(R.id.editText);
        number3 = (TextView) findViewById(R.id.editText2);
        number4 = (TextView) findViewById(R.id.editText3);
        number5 = (TextView) findViewById(R.id.editText4);
        pickContact = (ImageView) findViewById(R.id.add_contact_image_view);
        pickContact2 = (ImageView) findViewById(R.id.add_contact_image_view2);
        pickContact3 = (ImageView) findViewById(R.id.add_contact_image_view3);
        pickContact4 = (ImageView) findViewById(R.id.add_contact_image_view4);
        pickContact5 = (ImageView) findViewById(R.id.add_contact_image_view5);
        if (obj != null) {

            Toast.makeText(Contacts.this, "CONTACTS already stored", Toast.LENGTH_LONG).show();
            Cursor res = database.getAllData();
            if (res.moveToNext()) {
                String ph1 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p1)));
                number1.setText(ph1);
                String ph2 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p2)));
                number2.setText(ph2);
                String ph3 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p3)));
                number3.setText(ph3);
                String ph4 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p4)));
                number4.setText(ph4);
                String ph5 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p5)));
                number5.setText(ph5);
            }

        } else {
            number1 = (TextView) findViewById(R.id.phone_number_edit_text);
            number2 = (TextView) findViewById(R.id.editText);
            number3 = (TextView) findViewById(R.id.editText2);
            number4 = (TextView) findViewById(R.id.editText3);
            number5 = (TextView) findViewById(R.id.editText4);
            pickContact = (ImageView) findViewById(R.id.add_contact_image_view);
            pickContact2 = (ImageView) findViewById(R.id.add_contact_image_view2);
            pickContact3 = (ImageView) findViewById(R.id.add_contact_image_view3);
            pickContact4 = (ImageView) findViewById(R.id.add_contact_image_view4);
            pickContact5 = (ImageView) findViewById(R.id.add_contact_image_view5);

            EnableRuntimePermission();

            pickContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 7);
                    f1 = true;

                }
            });
            pickContact2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 7);
                    f2 = true;
                }
            });
            pickContact3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 7);
                    f3 = true;
                }
            });
            pickContact4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 7);
                    f4 = true;
                }
            });
            pickContact5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 7);
                    f5 = true;
                }
            });
        }
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Contacts.this,
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(Contacts.this, "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Contacts.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(Contacts.this, "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Contacts.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "";
                    int IDresultHolder;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult);

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                //name.setText(TempNameHolder);
                                if (f1) {
                                    number1.setText(TempNumberHolder);
                                    f1 = false;
                                }
                                if (f2) {
                                    number2.setText(TempNumberHolder);
                                    f2 = false;
                                }
                                if (f3) {
                                    number3.setText(TempNumberHolder);
                                    f3 = false;
                                }
                                if (f4) {
                                    number4.setText(TempNumberHolder);
                                    f4 = false;
                                }
                                if (f5) {
                                    number5.setText(TempNumberHolder);
                                    f5 = false;
                                }
                            }
                        }

                    }
                }
                break;
        }
    }
    public void begin(View view)
    {
        SqDB database=new SqDB(this);
        TextView p1,p2,p3,p4,p5;
        p1=(EditText) findViewById(R.id.phone_number_edit_text);
        p2=(EditText) findViewById(R.id.editText);
        p3=(EditText) findViewById(R.id.editText2);
        p4=(EditText) findViewById(R.id.editText3);
        p5=(EditText) findViewById(R.id.editText4);
        if(TextUtils.isEmpty(p1.getText().toString()) || TextUtils.isEmpty(p2.getText().toString())||TextUtils.isEmpty(p3.getText().toString())||TextUtils.isEmpty(p4.getText().toString())||TextUtils.isEmpty(p5.getText().toString()))
        {
            Toast.makeText(this, "One or more fields empty", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean result = database.insertNumber(p1.getText().toString(), p2.getText().toString(), p3.getText().toString(), p4.getText().toString(), p5.getText().toString());
            if (result) {
                Toast.makeText(Contacts.this, "Contacts stored in database", Toast.LENGTH_LONG).show();
                finish();

            } else {
                Toast.makeText(Contacts.this, "Contacts not stored in database", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


