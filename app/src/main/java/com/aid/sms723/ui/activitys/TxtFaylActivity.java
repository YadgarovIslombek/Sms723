package com.aid.sms723.ui.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.aid.sms723.FileManager;
import com.aid.sms723.R;
import com.aid.sms723.adapter.NumberAdapter;
import com.aid.sms723.model.Number;
import com.aid.sms723.model.UserData;
import com.aid.sms723.util.LocalStorage;
import com.aid.sms723.util.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TxtFaylActivity extends AppCompatActivity {
    private static final int FILE_EXPLORER_CODE = 10;
    private static final String SENT = "";
    private static final String DELIVERED = "";
    private boolean keepSending = true;
    private String TAG = "TxtFaylActivity";
    AlertDialog dialog;
    RecyclerView recyclerView;
    ArrayList<Number> numberList;
    NumberAdapter adapter;
    Context context;
    TextView totalList, toolbartxt;
    AppCompatButton btnAdd, btnSend;
    PendingIntent sentPI;
    EditText editText;
    PendingIntent deliveredPI;
    ArrayList<PendingIntent> sentIntents;
    ArrayList<PendingIntent> deliveryIntents;
    String asosiyLimit;
    private ArrayList<String> numbers;
    int smsQoldiq;
    private int iterator = 0;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private TextView progress_text;
    private Button cancel;
    ImageView imageView;
    BroadcastReceiver broadcastReceiver;
    private boolean sentToSettings = false;
    private static final int SMS_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    LocalStorage localStorage;
    LinearLayout loading_panel, liner1 ;
    CardView cardView;
    ImageView imgDelete;
    int smsSENT, smsFAILED;
    public int smsSuccess,smsGenericfailure,smsNoService,smsNullPdu,smsRadioOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_fayl);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        Toolbar toolbarr = findViewById(R.id.toolbar);
        toolbartxt = toolbarr.findViewById(R.id.toolbar_title);
        localStorage = new LocalStorage(getApplicationContext());
        toolbartxt.setText("Fayl tanla");
        btnSend = findViewById(R.id.btnSend);
        btnSend.setEnabled(false);
        cardView = findViewById(R.id.cardview);
        totalList = findViewById(R.id.totalList);

        loading_panel = (LinearLayout) findViewById(R.id.loading_panel);
        liner1 = findViewById(R.id.liner1);
        progress_text = (TextView) findViewById(R.id.progress_txt);
        cancel = (Button) findViewById(R.id.cancel_button);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new EventChooseFile());
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.rv);
        imgDelete = findViewById(R.id.imgDelete);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        checkAndroidPermission();
//        broadcastReceiver = new NetworkChangeReciever();
//        registerBroadcastReciver();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                assert userData != null;
//                userid.setText(userData.getUserId());
                asosiyLimit = userData.getLimit().toString();
                Log.d("Limit", asosiyLimit);

                smsQoldiq = Integer.parseInt(asosiyLimit);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 if (editText.length() == 0){
                    Toast.makeText(TxtFaylActivity.this, "Xabar yozing", Toast.LENGTH_SHORT).show();
                }
                else {
                    SENDMESSAGE();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keepSending = false;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int a = smsQoldiq-smsSuccess;
                        writeFire(String.valueOf(a));
                    }
                },2000);
                allSMSSent();


            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numbers.size() > 0) {
                    new MaterialAlertDialogBuilder(TxtFaylActivity.this, R.style.AlertDialogTheme)
                            .setTitle("O'chirish")
                            .setMessage("Listni tozalashni hohlaysizmi?")
                            .setPositiveButton("HA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clear();
                                    totalList.setText("Jami " + numbers.size() + " ta raqam");
                                    Log.i("PROSTA.", String.valueOf(numbers.size()));
                                }
                            }).setNegativeButton("Yo'q", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                } else {
                    Toast.makeText(TxtFaylActivity.this, "List bo'sh", Toast.LENGTH_SHORT).show();
                }

            }
        });


        }
    public void clear() {
        int size = numbers.size();
        numbers.clear();
        totalList.setText("0 ta");
        adapter.notifyItemRangeRemoved(0,size);
        adapter.notifyDataSetChanged();

    }
    //Permission
    private void checkAndroidPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            // write your logic here
            Toast.makeText(context, ".", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(TxtFaylActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(TxtFaylActivity.this,
                        Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.SEND_SMS)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TxtFaylActivity.this);
                builder.setTitle("ILOVA RUXSATI");
                builder.setMessage("Ilovadan foydalanish uchun SEND_SMS va READ_EXTERNAL_STORAGE ga ruxsat berishingiz kerak.");
                builder.setPositiveButton("Ha", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(
                                new String[]{Manifest.permission
                                        .READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS},
                                PERMISSIONS_MULTIPLE_REQUEST);
                    }
                });
                builder.setNegativeButton("Yoq", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE,  Manifest.permission.SEND_SMS},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternalFile)
                    {

                        // write your logic here
                        Toast.makeText(TxtFaylActivity.this, "Ruxsat etildi", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Ilovadan foydalanish uchun ruxsat bering",
                                Snackbar.LENGTH_INDEFINITE).setAction("O'TISH",
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }






//
//    public void ReadFire() {
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserData userData = snapshot.getValue(UserData.class);
//                assert userData != null;
////                userid.setText(userData.getUserId());
//               asosiyLimit = userData.getLimit().toString();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void writeFire(String a) {
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("limit", a);

        databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "Limit qol", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void SENDMESSAGE() {
        hideButtonPanel();
        sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);
         sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        sentIntents.add(sentPI);
        deliveryIntents.add(deliveredPI);

//        ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        smsSuccess++;
                        Log.d("SanoqSucs",String.valueOf(smsSuccess));
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        smsGenericfailure++;
                        Log.d("SanoqGEn",String.valueOf(smsGenericfailure));
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        smsNoService++;
                        Log.d("SanoqNO",String.valueOf(smsNoService));
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        smsNullPdu++;
                        Log.d("SanoqNULL",String.valueOf(smsNullPdu));
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        smsRadioOff++;
                        Log.d("SanoqRad",String.valueOf(smsRadioOff));
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS yetkazildi",
//                                Toast.LENGTH_LONG).show();
                        smsSENT++;
                        break;
                    case Activity.RESULT_CANCELED:
                        smsFAILED++;
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        try {
            runSendMessage();
        } catch (Exception e) {
            Log.d("Error: ", "" + e);
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void runSendMessage() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (keepSending) {
                            if(iterator >= numbers.size()){
                                OddiyPanel();
                                allSMSSent();
                                int a = smsQoldiq-smsSuccess;
                                writeFire(String.valueOf(a));
                                Log.d("Sms count2", String.valueOf(smsQoldiq));
                                keepSending=false;
                            }else {
                                try {
                                    // checkDelimiterMode();//check the array split delimiter symbol
//                                    for (int i = 0; i < numbers.size(); i++) {

//                                    }
//                                        writeFire(String.valueOf(smsQoldiq));
                                    SmsManager sms = SmsManager.getDefault();
                                    ArrayList<String> message = sms.divideMessage(editText.getText().toString());
                                    sms.sendMultipartTextMessage(numbers.get(iterator), null, message, sentIntents,deliveryIntents );
                                    progress_text.setText(numbers.get(iterator) + " ga sms yuborilyapti " + "  , Jami yuborilgan(" + String.valueOf(iterator) + "/" + numbers.size() + ")");
                                    iterator++;
                                    new Handler(Looper.getMainLooper()).postDelayed(this,5000);

                                } catch (Exception e) {
                                    Log.d("Error: ", "" + e);
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            keepSending= false;

                        }
                    }
                });
            }
        }, 5000);

    }


     class EventChooseFile implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/*");
            try {
                startActivityForResult(intent, FILE_EXPLORER_CODE);
            } catch (android.content.ActivityNotFoundException e) {
                e.getMessage();
            }
        }
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == FILE_EXPLORER_CODE) {
            if (data != null && data.getData() != null) {
                FileManager fileManager = new FileManager();
                try {
                    ArrayList<String> numberModellist = fileManager.ReadNumbers(Objects.requireNonNull(getApplicationContext()), data.getData());
                    Log.d("NumSizListSize", String.valueOf(numberModellist));
                    numbers = numberModellist;
                    btnSend.setEnabled(true);
                    Log.d(TAG, asosiyLimit.toString());
                    if (numberModellist.size() > Integer.parseInt(asosiyLimit)) {
                        new MaterialAlertDialogBuilder(TxtFaylActivity.this,R.style.AlertDialogTheme)
                                .setTitle("LIMIT KAM")
                                .setMessage("Hozirda limitlar soni - " + asosiyLimit)
                                .setPositiveButton("Limit sotib olish", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(TxtFaylActivity.this,PaymentActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNeutralButton("Yopish", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                        recyclerView.removeAllViewsInLayout();
                    } else {
                        if (numberModellist.size() == 0) {
                            new MaterialAlertDialogBuilder(TxtFaylActivity.this,R.style.AlertDialogTheme)
                                    .setTitle("XATOLIK")
                                    .setMessage(".txt fayl ichidagi malumot o'qishda xatolik \n Namuna: +998901002222")
                                    .setPositiveButton("Yopish", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                            //recyler viewni obnovit qilibarish garak yani tozalab!!!!!
                            recyclerView.removeAllViewsInLayout();
                        } else {
                            numberList = fileManager.createFiles(numberModellist, numberModellist.size());
                            Log.d("SizListSize", String.valueOf(numberModellist.size()));
                            totalList.setText(String.valueOf("Jami " + numberModellist.size() + " Contact"));
                            adapter = new NumberAdapter(numberList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void changeActionBarTitle(ActionBar actionBar) {
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(getApplicationContext());
        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        // Set text to display in TextView
        tv.setText("Mahsulotlar"); // ActionBar title text
        tv.setTextSize(19);
        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.white));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }

    private void OddiyPanel() {
        loading_panel.setVisibility(View.GONE);
        liner1.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.VISIBLE);
        totalList.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        if(loading_panel.getVisibility() == View.VISIBLE){
            new MaterialAlertDialogBuilder(TxtFaylActivity.this,R.style.AlertDialogTheme)
                    .setTitle("")
                    .setMessage("Bekor qilmoqchimisiz?")
                    .setPositiveButton("Ha", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TxtFaylActivity.super.onBackPressed();
                            keepSending = false;
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int a = smsQoldiq-smsSuccess;
                                    writeFire(String.valueOf(a));
                                    overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                                }
                            },3000);
                            allSMSSent();

                        }
                    })
                    .setNegativeButton("Yoq", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.right_enter,R.anim.left_out);
        }

            }

    private void  hideButtonPanel(){
        loading_panel.setVisibility(View.VISIBLE);
        liner1.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);
        totalList.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
    }
    private void allSMSSent() {
        loading_panel.setVisibility(View.GONE);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TxtFaylActivity.this);
        View layoutView = LayoutInflater.from(TxtFaylActivity.this).inflate(R.layout.result_dialog, null);
        alertDialog.setView(layoutView);
        TextView totalsuccessCount, totalFailedCount;
        totalsuccessCount = layoutView.findViewById(R.id.totalsuccessCount);
        totalFailedCount = layoutView.findViewById(R.id.totalFailedCount);
        String smssuccesss;
        String smsfaileddd;
        smssuccesss = String.valueOf(smsSuccess);
        smsfaileddd = String.valueOf(smsFAILED);
        totalsuccessCount.setText(smssuccesss.toString());
        totalFailedCount.setText(smsfaileddd.toString());
        Button button = layoutView.findViewById(R.id.btnok);
        dialog = alertDialog.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ni = new Intent(TxtFaylActivity.this, MainActivity.class);
                startActivity(ni);
                finish();
                dialog.dismiss();
            }
        });
    }
//    protected void registerBroadcastReciver(){
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        }
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        }
//    }
//    protected void unregister(){
//        try{
//            unregisterReceiver(broadcastReceiver);
//
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }
//    }


}