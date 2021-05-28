package com.aid.sms723.ui.activitys;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.aid.sms723.R;
import com.aid.sms723.ui.fragments.ChangeNameFragmentDialog;
import com.aid.sms723.ui.fragments.KabinetFragment;
import com.aid.sms723.util.LocalStorage;
import com.aid.sms723.util.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressbarTolov;
    CardView cardOddiy, cardSilver, cardGold;
    ConstraintLayout ConfirmPayment, Tolov, asosiy;
    String PaketName;
    int bbb;
    String Limit;
    String randomCode;
    TextView limit, paketName, price, toolbartxt, limitSell;
    AppCompatButton btnPayment, btn_tasdiqCode;
    LocalStorage localStorage;
    String localLimit;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    EditText editText;
    long b;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        broadcastReceiver = new NetworkChangeReciever();
        registerBroadcastReciver();
        localStorage = new LocalStorage(getApplicationContext());
        Toolbar toolbarr = findViewById(R.id.toolbarTolov);
        Toolbar toolbar1 = findViewById(R.id.toolbar121);
        toolbartxt = toolbarr.findViewById(R.id.toolbar_title);
        progressbarTolov = findViewById(R.id.progressbarTolov);
        toolbartxt.setText("To'lov");
        toolbartxt = toolbar1.findViewById(R.id.toolbar_title);
        toolbartxt.setText("To'lov");
        cardOddiy = findViewById(R.id.cardOddiy);
        cardSilver = findViewById(R.id.cardSilver);
        cardGold = findViewById(R.id.cardGold);
        btnPayment = findViewById(R.id.btnPayment);
        paketName = findViewById(R.id.paketName);
        price = findViewById(R.id.price);
        ConfirmPayment = findViewById(R.id.ConfirmPayment);
        Tolov = findViewById(R.id.Tolov);
        asosiy = findViewById(R.id.asosiy);
        limit = findViewById(R.id.limit);
        editText = findViewById(R.id.input_kod);
        btn_tasdiqCode = findViewById(R.id.btn_tasdiqCode);
        limitSell = findViewById(R.id.limitSell);
        cardOddiy.setOnClickListener(this);
        btn_tasdiqCode.setOnClickListener(this);
        cardSilver.setOnClickListener(this);
        cardGold.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        asosiy.setVisibility(View.VISIBLE);
        ConfirmPayment.setVisibility(View.GONE);
        Tolov.setVisibility(View.GONE);
        localLimit = localStorage.getLimit().toString();
//        Log.d("Loggg", localLimit);
        limit.setText(localLimit);
        bbb++;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        editText.setOnTouchListener((v1, event ) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    new MaterialAlertDialogBuilder(PaymentActivity.this,R.style.AlertDialogTheme)
                            .setTitle("TASDIQLASH KOD")
                            .setMessage("To'lovni amalga oshirdingizmi? unda biz bilan bo'glaning va kodni oling!")
                            .setPositiveButton("Ha", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                    return true;
                }
            }

            return false;
        });


    }

    private void registerBroadcastReciver() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    protected void unregister(){
        try{
            unregisterReceiver(broadcastReceiver);

        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardOddiy:
                progressbarTolov.setVisibility(View.VISIBLE);
                asosiy.setVisibility(View.GONE);
                ConfirmPayment.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tolov.setVisibility(View.VISIBLE);
                                progressbarTolov.setVisibility(View.GONE);
                                String p = "Oddiy";
                                String p1 = "59 000";
                                String p5 = "5000";
                                paketName.setText(p.toString());
                                limitSell.setText(p5);
                                price.setText(p1.toString() + " so'm");
                            }
                        });
                    }
                },1000);
                break;
            case R.id.cardSilver:
                asosiy.setVisibility(View.GONE);
                progressbarTolov.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tolov.setVisibility(View.VISIBLE);
                                progressbarTolov.setVisibility(View.GONE);
                                String p2 = "Silver";
                                String p3 = "100 000 ";
                                String p4 = "10000";
                                paketName.setText(p2.toString());
                                limitSell.setText(p4);
                                price.setText(p3.toString() + " so'm");
                            }
                        });
                    }
                },1000);



                break;
            case R.id.cardGold:
                asosiy.setVisibility(View.GONE);
               progressbarTolov.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tolov.setVisibility(View.VISIBLE);
                                progressbarTolov.setVisibility(View.GONE);
                                String p6 = "Gold";
                                String p7 = "229 000";
                                String p8 = "25000";
                                paketName.setText(p6.toString());
                                price.setText(p7.toString() + " so'm");
                                limitSell.setText(p8);
                            }
                        });
                    }
                },1000);
                break;
            case R.id.btnPayment:
                String limitSellString = limitSell.getText().toString().trim();
                localStorage.saveLimitNewSell(limitSellString.toString());
//                Log.d("new", limitSellString);
                new MaterialAlertDialogBuilder(PaymentActivity.this, R.style.AlertDialogTheme)
                        .setTitle("To'lov")
                        .setMessage("To'lovni amalga oshirdingizmi?")
                        .setPositiveButton("HA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Tolov.setVisibility(View.GONE);
                                progressbarTolov.setVisibility(View.VISIBLE);
                                SecureRandom random = new SecureRandom();
                                randomCode = new BigInteger(30, random).toString(32).toUpperCase();
                                Date currentTime = Calendar.getInstance().getTime();
                                long a = (long) currentTime.getTime();
                                b = a + 22 + 23 + 14 - 100705;
                                String d = String.valueOf(b);
                                String userId = firebaseUser.getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("confirmCod", randomCode);
                                databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressbarTolov.setVisibility(View.GONE);
                                        ConfirmPayment.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }).setNegativeButton("Yo'q", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressbarTolov.setVisibility(View.GONE);
                        dialogInterface.dismiss();
                    }
                }).show();
                break;
            case R.id.btn_tasdiqCode:
                String limSell = localStorage.getLimitNewSell().toString();

                String limitOld = localStorage.getLimit();
//                Log.d("LimitSEll", limSell);
//                Log.d("LimitOld", limitOld);

                int old = Integer.parseInt(limitOld);
                int newsell = Integer.parseInt(limSell);
                String obshiy = String.valueOf(old + newsell);
                String tekshir = editText.getText().toString().trim();
                progressbarTolov.setVisibility(View.VISIBLE);
                ConfirmPayment.setVisibility(View.GONE);
                if (editText.length() != 0) {
                    progressbarTolov.setVisibility(View.VISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (randomCode.contains(tekshir)) {
                                progressbarTolov.setVisibility(View.GONE);
                                new MaterialAlertDialogBuilder(PaymentActivity.this, R.style.AlertDialogTheme)
                                        .setTitle("Tabriklaymiz")
                                        .setCancelable(false)
                                        .setMessage(limSell.toString() + " ta limitga ega bo'ldingiz")
                                        .setPositiveButton("Hop", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressbarTolov.setVisibility(View.VISIBLE);
                                                String userId = firebaseUser.getUid();
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("limit", obshiy);
                                                hashMap.put("confirmCod", "Ok");
//                                                Log.d("OBshiy", obshiy);
                                                databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Tolov.setVisibility(View.GONE);
                                                        progressbarTolov.setVisibility(View.GONE);

                                                    }
                                                });
                                                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).show();
                            } else {
                                progressbarTolov.setVisibility(View.GONE);
                                ConfirmPayment.setVisibility(View.VISIBLE);
                                Toast.makeText(PaymentActivity.this, "Kiritilgan tasdiqlash kodi xato!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },1500);

                }else{
                    progressbarTolov.setVisibility(View.GONE);
                    ConfirmPayment.setVisibility(View.VISIBLE);
                    Toast.makeText(PaymentActivity.this, "Tasdiqlash kodi kiritilmagan", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left,R.anim.right_out);
    }
}