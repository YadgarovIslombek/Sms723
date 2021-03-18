package com.aid.sms723.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aid.sms723.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private MaterialEditText input_phonenumber,_passwordText, _nameText, _emailText,input_imei;
    private Button _signupButton;
    ProgressBar progressBar;
    TextView phonenumbertxtll ;
    private FirebaseAuth mAuth;
    LinearLayout link_login;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbarS);
        link_login = findViewById(R.id.link_login);
        _nameText =  findViewById(R.id.input_name);
        _emailText =  findViewById(R.id.input_email);
        input_imei = findViewById(R.id.input_imei);
        _passwordText =  findViewById(R.id.input_password);
        input_phonenumber =  findViewById(R.id.input_phonenumber);
        _signupButton = findViewById(R.id.btn_signup);
        phonenumbertxtll = findViewById(R.id.phonenumbertextll);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = _nameText.getText().toString().trim();
                final String user_email  = _emailText.getText().toString().trim();
                input_imei.setText("212121212121");
                final String imei  = input_imei.getText().toString().trim();

                final String user_parol  = _passwordText.getText().toString().trim();
                final String user_phone  = input_phonenumber.getText().toString().trim();
                if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_email) || TextUtils.isEmpty(imei) ||
                        TextUtils.isEmpty(user_parol) || TextUtils.isEmpty(user_phone)){
                    Toast.makeText(SignUpActivity.this, "Maydonlarni to'ldiring", Toast.LENGTH_SHORT).show();
                }else{
                    register(user_name,user_email,imei,user_parol,user_phone);
                }
            }
        });




}

    private void register(String user_name, String user_email, String imei, String user_parol, String user_phone) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user_email,user_parol).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userId = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashMap = new HashMap();
                    hashMap.put("userId",userId);
                    hashMap.put("username",user_name);
                    hashMap.put("mail",user_email);
                    hashMap.put("imei",imei);
                    hashMap.put("password",user_parol);
                    hashMap.put("phone",user_phone);
                    hashMap.put("limit","delaulf");
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    }
