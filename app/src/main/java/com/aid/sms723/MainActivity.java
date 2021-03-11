package com.aid.sms723;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    RecyclerView recyclerView;
    int defaultLimit =7;
    private static final int FILE_EXPLORER_CODE = 10;
    ArrayList<Number> numberList ;
    NumberAdapter adapter;
    Context context;
    Number number;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        button = findViewById(R.id.choosefile_btn);
        button.setOnClickListener(new EventChooseFile());

      recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        NumberAdapter adapter = new NumberAdapter(numberModel);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
       // Log.d("List", String.valueOf(numberModel.length));
       // textView.setText("Jami:  "+String.valueOf(numberModel.length) + " ta Contact");

    }

    public boolean TekshiruvPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            //==================Avval ruxsat berilgan bolsa ifni ichiga kiradi Start ==========-//////////
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                Log.v(TAG,"Ruxsat berilgan uje");
                new EventChooseFile();
                return true;
            }
            //==================Avval ruxsat berilgan bolsa ifni ichiga kiradi End ==========-//////////

            // ==================================================================================================================//

            //==================Agarda user app ga 1 kirib bekor qilsa Start ==========-//////////
            else{
                Log.d(TAG,"Bekor qilindi");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                return false;
            }
            //==================Agarda user app ga 1 kirib bekor qilsa End ==========-//////////
        }else{
            Log.v(TAG,"Ruxsat berildi");
            new EventChooseFile();
            return true;
        }
    }
    //=========================================External storage ochilik faylni tanladi Start=================================//
    private class EventChooseFile implements View.OnClickListener {

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
    //=========================================External storage ochilik faylni tanladi End =================================//

    //=========================================External storage Faylni ochib oqimiz Start=================================//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK && requestCode == FILE_EXPLORER_CODE){
            if(data != null && data.getData() != null){
                FileManager fileManager = new FileManager();
                try {
                    ArrayList<String> numberModellist = fileManager.ReadNumbers(this,data.getData());
                    if(numberModellist.size() == 0){
                        Utils.showNeutralAlertDialog(this,"Xatolik",".txt fayl ichidagi malumot o'qishda xatolik");
                        //recyler viewni obnovit qilibarish garak yani tozalab!!!!!
                        recyclerView.removeAllViewsInLayout();
                    }else {
                        numberList = fileManager.createFiles(numberModellist, numberModellist.size());
                        Log.d("SizListSize", String.valueOf(numberModellist.size()));
                        adapter = new NumberAdapter(numberList,this);
                        recyclerView.setAdapter(adapter);

                    }
//                    Log.d("ReadNumberMetod", String.valueOf(numberModellist));
//                    Log.d("CreateFilesNumberMetod", String.valueOf(numberList));
//                    Log.d("ListSize", String.valueOf(numberModellist.size()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 2:
                //==================Agarda user app ga 1 kirib ruxsat barsa Start ==========-//////////
                Log.d(TAG,"Ichki xotira");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Ruxsat  "+permissions[0]+ " bo " + grantResults[0]);
                    new EventChooseFile();
                }
                //==================Agarda user app ga 1 kirib ruxsat barsa End ==========-//////////
                else{

                }
                break;
        }
    }
}