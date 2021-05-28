package com.aid.sms723.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aid.sms723.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class ChangeNameFragmentDialog extends DialogFragment {
    public interface onInputName{
        void sendInput1(String s);
    }
    public onInputName onInputName;
    MaterialEditText newName;
    AppCompatButton btn_confirm, btn_bekor;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    public ChangeNameFragmentDialog() {
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
        View v = inflater.inflate(R.layout.fragment_change_name_dialog, container, false);
        progressBar = v.findViewById(R.id.progressbarRes);
        btn_bekor = v.findViewById(R.id.btn_bekor);
        btn_confirm = v.findViewById(R.id.btn_confirm);
        newName = v.findViewById(R.id.newName);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String txtnewName = Objects.requireNonNull(newName.getText()).toString().trim();
                if(TextUtils.isEmpty(txtnewName)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Maydonlarni toldiring", Toast.LENGTH_SHORT).show();
                }else{
                    String userId = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("username",txtnewName);
                    onInputName.sendInput1(txtnewName);
                    databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Ismingiz muffaqiyatli o'zgardi", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            getDialog().dismiss();
                        }
                    });

                }
//                progressBar.setVisibility(View.GONE);
            }



        });
        btn_bekor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();

            }
        });
        return v;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onInputName = (ChangeNameFragmentDialog.onInputName)getTargetFragment();

        }catch (ClassCastException e){
            Log.e("ChangeFrag","onAttach : ClasssCAsstExeption : " +  e.getMessage());
        }
    }

}