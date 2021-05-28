package com.aid.sms723.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.aid.sms723.model.UserData;
import com.aid.sms723.ui.activitys.LoginActivity;
import com.aid.sms723.util.LocalStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class ChangePasswordFragmentDialog extends DialogFragment {
        public interface onInputParol{
        void sendInput(String s);
    }
    public onInputParol onInputPar;
    DatabaseReference databaseReference;
    MaterialEditText oldpassword,newpass,confirmpass;
    AppCompatButton btn_confirm, btn_bekor;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Context  context;
    LocalStorage localStorage;
    private AlertDialog alertDialog = null;
    public ChangePasswordFragmentDialog() {
        // Required empty public constructor
    }

    public ChangePasswordFragmentDialog(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       // final LayoutInflater v = Objects.requireNonNull(getActivity()).getLayoutInflater();
       View v =  inflater.inflate(R.layout.fragment_change_pasword, container, false);
        progressBar = v.findViewById(R.id.progressbarRes);
        btn_bekor = v.findViewById(R.id.btn_bekor);
        oldpassword = v.findViewById(R.id.oldpassword);
        newpass = v.findViewById(R.id.newpas);
        confirmpass = v.findViewById(R.id.confirmpass);
        btn_confirm = v.findViewById(R.id.btn_confirm);
        btn_confirm = v.findViewById(R.id.btn_confirm);
        localStorage = new LocalStorage(getContext());
        String pass = localStorage.getSavePassword().toString();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtOld = oldpassword.getText().toString().trim();
                String txtnewPass = newpass.getText().toString().trim();
                String txtConF = confirmpass.getText().toString().trim();
                if(TextUtils.isEmpty(txtOld) || TextUtils.isEmpty(txtnewPass) || TextUtils.isEmpty(txtConF)){
                    Toast.makeText(getContext(), "Maydonlarni toldiring", Toast.LENGTH_SHORT).show();

                }else if(txtnewPass.length()<6){
                    Toast.makeText(getContext(), "Minimum 6 ta belgi", Toast.LENGTH_SHORT).show();
                }else if(!txtnewPass.equals(txtConF)){
                    Toast.makeText(getContext(), "Avvalgi parol bilan bir xil bo'lsin", Toast.LENGTH_SHORT).show();

                }
                else{
                    if(pass.equals(txtOld)) {
                        changepassword(txtOld, txtnewPass);
                        String userId = firebaseUser.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("password",txtnewPass);
                        databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(getContext(), "Parolingiz muffaqiyatli o'zgardi", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Parol xato " + pass, Toast.LENGTH_SHORT).show();
                    }

//                    KabinetFragment kabinetFragment = (KabinetFragment) Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("KabinetFragment");
//                    assert kabinetFragment != null;
//                    kabinetFragment.parolEd_pr.setText(txtnewPass.toString()+ " ");
                    onInputPar.sendInput(txtnewPass);
//                    Log.d("Log", "getdi");
                }


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
            onInputPar = (onInputParol)getTargetFragment();

        }catch (ClassCastException e){
//            Log.e("ChangeFrag","onAttach : ClasssCAsstExeption : " +  e.getMessage());
        }
    }

    private void changepassword(String txtOld, String txtnewPass) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential authCredential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()),txtOld);
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(txtnewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}