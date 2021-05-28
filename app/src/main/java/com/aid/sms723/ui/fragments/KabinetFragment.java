package com.aid.sms723.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aid.sms723.R;
import com.aid.sms723.model.UserData;
import com.aid.sms723.ui.activitys.LoginActivity;
import com.aid.sms723.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class KabinetFragment extends Fragment implements ChangePasswordFragmentDialog.onInputParol,ChangeNameFragmentDialog.onInputName {
    public MaterialEditText nameEd_pr;
    public MaterialEditText userid;
    MaterialEditText emailEd_pr;
    public MaterialEditText parolEd_pr;
    public MaterialEditText phoneEd_pr;
    MaterialEditText imeiEd_pr;
    MaterialEditText limitEd_pr;
    AppCompatButton btn_save;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    public void sendInput(String s) {
//        Log.d("Kabinet", "find incoming" + s);
        parolEd_pr.setText(s);

    }
    @Override
    public void sendInput1(String s){
        nameEd_pr.setText(s);

    }
    public KabinetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_kabinet, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //userid = v.findViewById(R.id.Userid);
        btn_save = v.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        nameEd_pr = v.findViewById(R.id.nameEd_pr);
        emailEd_pr = v.findViewById(R.id.emailEd_pr);
        parolEd_pr = v.findViewById(R.id.parolEd_pr);
        phoneEd_pr = v.findViewById(R.id.phoneEd_pr);
        limitEd_pr = v.findViewById(R.id.limitEd_pr);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                assert userData !=null;
//                userid.setText(userData.getUserId());
                nameEd_pr.setText(userData.getUsername());
                emailEd_pr.setText(userData.getMail());
                parolEd_pr.setText(userData.getPassword());
                phoneEd_pr.setText(userData.getPhone());
                limitEd_pr.setText(userData.getLimit());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        nameEd_pr.setOnTouchListener((v1, event ) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (parolEd_pr.getRight() - parolEd_pr.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                            ChangeNameFragmentDialog changeNameFragmentDialog = new ChangeNameFragmentDialog();
                            changeNameFragmentDialog.setTargetFragment(KabinetFragment.this, 1);
                            changeNameFragmentDialog.show(getParentFragmentManager(), "MainActivity");
                            return true;
                        }
                        }

            return false;
        });
        parolEd_pr.setOnTouchListener((v1, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (parolEd_pr.getRight() - parolEd_pr.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                   ChangePasswordFragmentDialog ChangePasswordFragmentDialog = new ChangePasswordFragmentDialog();
                   ChangePasswordFragmentDialog.setTargetFragment(KabinetFragment.this,1);
                    ChangePasswordFragmentDialog.show(getParentFragmentManager(),"MainActivity");

                    return true;
                }
            }
            return false;
        });

        limitEd_pr.setOnTouchListener((v1, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (limitEd_pr.getRight() - limitEd_pr.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    Utils.showNeutralAlertDialog(getContext(),"Diqqat! \n Limit bu nima?","Limit - bu imkoniyat!)Limit - siz bir bosish bilan koplab insonlarga xabar yuborishingiz mumkin! \n Shunday ekan limit qancha ko'p bolsa shuncha insonga xabar yubora olasiz.");
                    return true;
                }
            }
            return false;
        });

      return  v;
    }



}