package com.aid.sms723.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.aid.sms723.ui.activitys.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class change_paswordFragment extends DialogFragment {
        public interface onInputParol{
        void sendInput(String s);
    }
    public onInputParol onInputPar;

    MaterialEditText oldpassword,newpass,confirmpass;
    Button btn_confirm, btn_bekor;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Context  context;
    private AlertDialog alertDialog = null;
    public change_paswordFragment() {
        // Required empty public constructor
    }

    public change_paswordFragment(Context context) {
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
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtOld = oldpassword.getText().toString().trim();
                String txtnewPass = newpass.getText().toString().trim();
                String txtConF = confirmpass.getText().toString().trim();
                if(TextUtils.isEmpty(txtOld) || TextUtils.isEmpty(txtnewPass) || TextUtils.isEmpty(txtConF)){
                    Toast.makeText(getContext(), "Mayfonlarni toldiring", Toast.LENGTH_SHORT).show();

                }else if(txtnewPass.length()<6){
                    Toast.makeText(getContext(), "6 dan kop at chumo", Toast.LENGTH_SHORT).show();
                }else if(!txtnewPass.equals(txtConF)){
                    Toast.makeText(getContext(), "Birxil at lox", Toast.LENGTH_SHORT).show();

                }
                else{
                    changepassword(txtOld,txtnewPass);
//                    KabinetFragment kabinetFragment = (KabinetFragment) Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("KabinetFragment");
//                    assert kabinetFragment != null;
//                    kabinetFragment.parolEd_pr.setText(txtnewPass.toString()+ " ");
                    onInputPar.sendInput(txtnewPass);
                    Log.d("Log", "getdi");
                }
              getDialog().dismiss();

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
            Log.e("ChangeFrag","onAttach : ClasssCAsstExeption : " +  e.getMessage());
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
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }else{
                                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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