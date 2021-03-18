package com.aid.sms723.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aid.sms723.FileManager;
import com.aid.sms723.R;
import com.aid.sms723.adapter.NumberAdapter;
import com.aid.sms723.model.Number;
import com.aid.sms723.ui.activitys.MainActivity;
import com.aid.sms723.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {
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


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView = view.findViewById(R.id.text);
        button = view.findViewById(R.id.choosefile_btn);
        button.setOnClickListener(new EventChooseFile());

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return  view;
    }

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == FILE_EXPLORER_CODE){
            if(data != null && data.getData() != null){
                FileManager fileManager = new FileManager();
                try {
                    ArrayList<String> numberModellist = fileManager.ReadNumbers(Objects.requireNonNull(getContext()),data.getData());
                    if(numberModellist.size() == 0){
                        Utils.showNeutralAlertDialog(getContext(),"Xatolik",".txt fayl ichidagi malumot o'qishda xatolik");
                        //recyler viewni obnovit qilibarish garak yani tozalab!!!!!
                        recyclerView.removeAllViewsInLayout();
                    }else {
                        numberList = fileManager.createFiles(numberModellist, numberModellist.size());
                        Log.d("SizListSize", String.valueOf(numberModellist.size()));
                        adapter = new NumberAdapter(numberList,getContext());
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

                }
                //==================Agarda user app ga 1 kirib ruxsat barsa End ==========-//////////
                else{

                }
                break;
        }
    }

}