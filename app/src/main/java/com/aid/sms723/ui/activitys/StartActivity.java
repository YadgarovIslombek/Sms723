package com.aid.sms723.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.aid.sms723.R;
import com.aid.sms723.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {
    TextView userid,userName,useremail,userimei,userParol,userphone,userLimit;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        userid = findViewById(R.id.Userid);
        userName = findViewById(R.id.UserName);
        useremail = findViewById(R.id.Useremail);
        userimei = findViewById(R.id.Userimei);
        userParol = findViewById(R.id.Userparol);
        userphone = findViewById(R.id.Userphone);
        userLimit = findViewById(R.id.UserLimit);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                assert userData !=null;
                userid.setText(userData.getUserId());
                userName.setText(userData.getUsername());
                useremail.setText(userData.getMail());
                userimei.setText(userData.getImei());
                userParol.setText(userData.getPassword());
                userphone.setText(userData.getPhone());
                userLimit.setText(userData.getLimit());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}