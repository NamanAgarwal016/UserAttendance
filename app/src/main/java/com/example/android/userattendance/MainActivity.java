package com.example.android.userattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button btnCheckOtp;
    DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCheckOtp = (Button) findViewById(R.id.check_otp);
        final String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();

        btnCheckOtp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.enter_otp);
                final String valueEntered = text.getText().toString();

                rootRef.child("CodingEvent").child("CoordOTP").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String valueRecieved = dataSnapshot.getValue(String.class);

                        if (valueEntered.equals(valueRecieved)) {
                            TextView textView1 = (TextView) findViewById(R.id.final_msg);
                            textView1.setText("Attendance Marked Successfully");
                            rootRef.child("CodingEvent").child(android_id).setValue(android_id);
                        } else {
                            TextView textView2 = (TextView) findViewById(R.id.final_msg);
                            textView2.setText("Incorrect OTP");
                            EditText text = (EditText) findViewById(R.id.enter_otp);
                            text.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Failed to read value.",
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
