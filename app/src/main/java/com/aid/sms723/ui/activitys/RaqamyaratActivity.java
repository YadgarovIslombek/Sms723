package com.aid.sms723.ui.activitys;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aid.sms723.R;
import com.aid.sms723.adapter.PhoneAdapter;
import com.aid.sms723.model.UserData;
import com.aid.sms723.ui.CustomForLoopDialog;
import com.aid.sms723.ui.fragments.ForLoopDialogFragment;
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

import java.util.ArrayList;
import java.util.HashMap;

import static com.aid.sms723.ui.activitys.TxtFaylActivity.PERMISSIONS_MULTIPLE_REQUEST;

public class RaqamyaratActivity extends AppCompatActivity implements ForLoopDialogFragment.OnInputListener {
    private String TAG = "RaqamyaratActivity";
    private static final int FILE_EXPLORER_CODE = 10;
    private static final String SENT = "";
    private static final String DELIVERED = "";
    private boolean keepSending = true;
    int smsSENT, smsFAILED;
    AlertDialog dialog;
    PendingIntent sentPI;
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
    LinearLayout loading_panel,  liner1 ;
    CardView cardview;
    public int smsSuccess, smsGenericfailure, smsNoService, smsNullPdu, smsRadioOff;
    private static final int SMS_PERMISSION_CONSTANT = 1001;
    private static final int REQUEST_PERMISSION_SETTING = 1010;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 1230;
    private RecyclerView recyclerView;
    Context context;
    ArrayList<String> listtt;
    private TextView toolbartxt, totalList;
    private AppCompatButton btnAdd, btnSend,cancelbtn;
//    CustomForLoopDialog forDialog;
    LocalStorage localStorage;
    static String listsize;
    ImageView imgDelete;
    EditText editText;
    ArrayList<String> phonelist = new ArrayList<>();
    ArrayList<String> newItemslist = new ArrayList<>();
    ArrayList<String> oldList = new ArrayList<>();
    PhoneAdapter adapter = new PhoneAdapter(this, phonelist);
    FragmentManager fm = getSupportFragmentManager();

    @Override
    public void sendInput(ArrayList<String> input) {
//        Log.e(TAG, "got the input: " + input);
        phonelist = input;
        listsize = String.valueOf(phonelist.size());
//       Log.i("SADASDas",listsize);
        totalList.setText("Jami " + listsize + " ta raqam");
        if (input.size() > Integer.parseInt(asosiyLimit)) {
            new MaterialAlertDialogBuilder(RaqamyaratActivity.this,R.style.AlertDialogTheme)
                    .setTitle("LIMIT KAM")
                    .setMessage("Hozirda limitlar soni - " + asosiyLimit)
                    .setPositiveButton("Limit sotib olish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RaqamyaratActivity.this,PaymentActivity.class);
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
            totalList.setText("0 ta");
        } else {
            if (adapter.getItemCount() == 0) {
                adapter = new PhoneAdapter(getApplicationContext(), phonelist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                oldList = phonelist;
//            Log.e(TAG, "ifni ichi: " + "IF");
//
            } else {
//            Log.e(TAG, "elseni ichi: " + "ELSE");
                newItemslist = input;
                int position = oldList.size();
                newItemslist.addAll(oldList);
                adapter = new PhoneAdapter(getApplicationContext(), newItemslist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                oldList = newItemslist;
                int size = oldList.size();
                listsize = String.valueOf(size);
                String a = "Jami ";
                String b = " ta raqam";
                totalList.setText(a + listsize + b);

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raqamyarat);
        Toolbar toolbarr = findViewById(R.id.toolbar);
        toolbartxt = toolbarr.findViewById(R.id.toolbar_title);
        toolbartxt.setText("Raqam yarat");
        totalList = findViewById(R.id.totalList);
        recyclerView = findViewById(R.id.rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        btnAdd = findViewById(R.id.btnAdd);
        btnSend = findViewById(R.id.btnSend);
        editText = findViewById(R.id.editText);
        imgDelete = findViewById(R.id.imgDelete);
        liner1 = findViewById(R.id.liner1);
        cardview = findViewById(R.id.cardview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        loading_panel = findViewById(R.id.loading_panel);
        progress_text = findViewById(R.id.progress_txt);
        cancelbtn = findViewById(R.id.cancel_button);
        checkAndroidPermission();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
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


        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldList.size() > 0) {
                    new MaterialAlertDialogBuilder(RaqamyaratActivity.this, R.style.AlertDialogTheme)
                            .setMessage("Listni tozalashni xohlaysizmi?")
                            .setPositiveButton("HA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clear();
                                    totalList.setText("Jami " + oldList.size() + " ta raqam");
                                    Log.i("PROSTA.", String.valueOf(oldList.size()));
                                }
                            }).setNegativeButton("Yo'q", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                } else {
                    Toast.makeText(RaqamyaratActivity.this, "List bo'sh", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForLoopDialogFragment forLoopDialogFragment = new ForLoopDialogFragment();
                forLoopDialogFragment.show(fm, "Dialog fragment");

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldList.size() == 0 ) {
                    Toast.makeText(RaqamyaratActivity.this, "List bo'sh", Toast.LENGTH_SHORT).show();
                }else if (editText.length() == 0){
                    Toast.makeText(RaqamyaratActivity.this, "Xabar yozing", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendSms();
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
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

}
    private void sendSms() {
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
                         //      .. Toast.LENGTH_LONG).show();

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS yetkazilmadi",
               //                 Tot.LENGTH_LONG).show();
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
                            if(iterator >= oldList.size()){
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
                                    sms.sendMultipartTextMessage(oldList.get(iterator), null, message, sentIntents,deliveryIntents );
                                    progress_text.setText(oldList.get(iterator) + " ga sms yuborilyapti " + "  , Jami yuborilgan(" + String.valueOf(iterator) + "/" + oldList.size() + ")");
                                    iterator++;
                                    new Handler(Looper.getMainLooper()).postDelayed(this,3000);

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
        }, 3000);

    }

    private void allSMSSent() {
        loading_panel.setVisibility(View.GONE);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RaqamyaratActivity.this);
        View layoutView = LayoutInflater.from(RaqamyaratActivity.this).inflate(R.layout.result_dialog, null);
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
                Intent ni = new Intent(RaqamyaratActivity.this, MainActivity.class);
                startActivity(ni);
                finish();
                dialog.dismiss();
            }
        });
    }


    private void OddiyPanel() {
        loading_panel.setVisibility(View.GONE);
        liner1.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.VISIBLE);
        totalList.setVisibility(View.VISIBLE);
        imgDelete.setVisibility(View.VISIBLE);
        cardview.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
    }

    private void  hideButtonPanel(){
        loading_panel.setVisibility(View.VISIBLE);
        liner1.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);
        totalList.setVisibility(View.GONE);
        imgDelete.setVisibility(View.GONE);
        cardview.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
    }

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


    public void clear() {
        int size = oldList.size();
        oldList.clear();
        totalList.setText("0 ta");
        adapter.notifyItemRangeRemoved(0,size);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("qaysi","onStart");

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("qaysi","onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("qaysi","onDestroy");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("qaysi","onResume");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("qaysi","onPause");

    }
    private void checkAndroidPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            // write your logic here
            Toast.makeText(context, ".", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if(loading_panel.getVisibility() == View.VISIBLE){
            new MaterialAlertDialogBuilder(RaqamyaratActivity.this,R.style.AlertDialogTheme)
                    .setTitle("")
                    .setMessage("Bekor qilmoqchimisiz?")
                    .setPositiveButton("Ha", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RaqamyaratActivity.super.onBackPressed();
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(RaqamyaratActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(RaqamyaratActivity.this,
                        Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.SEND_SMS)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RaqamyaratActivity.this);
                builder.setTitle("Ilova Ruhsati");
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
                        Toast.makeText(RaqamyaratActivity.this, "Ruxsat", Toast.LENGTH_SHORT).show();
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


}