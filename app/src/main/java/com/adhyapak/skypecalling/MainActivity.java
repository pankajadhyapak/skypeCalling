package com.adhyapak.skypecalling;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    private static final String TAG = "MainActivity";
    Boolean firstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        firstTime = true;

        Log.e(TAG, "onResume: "+ firstTime );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("skypeId");
        myRef.addValueEventListener(this);
    }

    private void callSkype(String skypeName) {
        Intent sky = new Intent("android.intent.action.VIEW");
        sky.setData(Uri.parse("skype:" + skypeName));

        if (sky.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(sky,1);
        } else {
            Toast.makeText(MainActivity.this, "No Skype App Installed ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String skypeId = "";
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Log.e(TAG, "Value is: " + postSnapshot.getKey());
            if (postSnapshot.getKey().equalsIgnoreCase("id")) {
                skypeId = postSnapshot.getValue(String.class);
                Log.e(TAG, "Value is: " + postSnapshot.getValue(String.class));
            }
        }
        if (skypeId.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username is Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (!firstTime) {
                Log.e(TAG, "Not first time: ");
                callSkype(skypeId);
            }
        }
        firstTime = false;

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, "Failed to read value.", databaseError.toException());
    }
}
