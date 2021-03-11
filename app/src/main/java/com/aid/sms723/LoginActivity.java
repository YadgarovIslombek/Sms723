package com.aid.sms723;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aid.sms723.databinding.ActivityLoginBinding;
import com.aid.sms723.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    //agar code xato bolsa yoki istek qayta yuborish uchun
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationID;
    private FirebaseAuth firebaseAuth;
    private static  final String TAG = "MAIN TAG";
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.linearLayout1.setVisibility(View.VISIBLE);
        binding.linearLayout11.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Kuting.....");
        progressDialog.setCanceledOnTouchOutside(false);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationID, forceResendingToken);
                Log.d(TAG, "onCodeSent: " + verificationID);

                mVerificationID = verificationID;
                forceResendingToken= token;
                progressDialog.dismiss();

                binding.linearLayout1.setVisibility(View.GONE);
                binding.linearLayout11.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this, "Tasdiqlash kodi yuborildi..........", Toast.LENGTH_SHORT).show();
            }
        };

        binding.phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String phone = binding.phoneEd.getText().toString().trim();
               if(TextUtils.isEmpty(phone)){
                   Toast.makeText(LoginActivity.this, "Telefon raqam kiriting.....", Toast.LENGTH_SHORT).show();
               }else{
                   startPhoneNumberVerification(phone);
               }
            }
        });

        binding.resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.phoneEd.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(LoginActivity.this, "Telefon raqam kiriting.....", Toast.LENGTH_SHORT).show();
                }else{
                    resendVerificationCode(phone, forceResendingToken);
                }
            }
        });
        binding.CodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.kod.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(LoginActivity.this, "Kod kiriting......", Toast.LENGTH_SHORT).show();
                }else{
                    VerifyPhoneNumber(mVerificationID,code);
                }
            }
        });
    }


    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Qaytadan code");
        progressDialog.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .setForceResendingToken(token)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void startPhoneNumberVerification(String phone) {
        progressDialog.setMessage("Raqam verify");
        progressDialog.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void VerifyPhoneNumber(String verificationID, String code) {
        progressDialog.setMessage("Tasdiqlash code");
        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("Kuting.....");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //succesfully kirsa
                        progressDialog.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(LoginActivity.this, "Succesfully " + phone, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,ProfileActivity2.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //xatolik
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "xatolik", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}