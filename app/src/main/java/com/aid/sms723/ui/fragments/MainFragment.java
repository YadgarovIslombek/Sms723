package com.aid.sms723.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aid.sms723.R;
import com.aid.sms723.model.UserData;
import com.aid.sms723.ui.LoadingDialog;
import com.aid.sms723.ui.activitys.InfoActivity;
import com.aid.sms723.ui.activitys.MainActivity;
import com.aid.sms723.ui.activitys.PaymentActivity;
import com.aid.sms723.ui.activitys.RaqamyaratActivity;
import com.aid.sms723.ui.activitys.TxtFaylActivity;
import com.aid.sms723.util.LocalStorage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainFragment extends Fragment {
    CardView cardTanla,cardCreate,cardInfo;
    TextView limitTxt;
    String limit;
    LinearLayout cardView2;
    UserData userData = new UserData();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    LoadingDialog loadingDialog;
    LocalStorage localStorage;
    public MainActivity activity;
    public MainFragment() {
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
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        localStorage = new LocalStorage(Objects.requireNonNull(getContext()));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cardTanla = view.findViewById(R.id.cardTanla);
        cardCreate = view.findViewById(R.id.cardCreate);
        cardInfo  = view.findViewById(R.id.cardInfo);
        limitTxt = view.findViewById(R.id.limittxtGold);
        cardView2 = view.findViewById(R.id.cardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(getContext(), PaymentActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.left_out);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        loadingDialog.startLoadingDialog();
        Log.d("Dialog","start");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                assert userData !=null;
//                userid.setText(userData.getUserId());
                    limitTxt.setText(userData.getLimit());
                    loadingDialog.dismissDialog();
                    localStorage.saveLimit(userData.getLimit().toString());
                    Log.d("Dialog","dissmis");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                try {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
        cardTanla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TxtFaylActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.left_enter,R.anim.right_out);

            }
        });
        cardCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RaqamyaratActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_enter,R.anim.right_out);
            }
        });
        cardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =  new Intent(getContext(), InfoActivity.class);
               startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_enter,R.anim.right_out);
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(userData == null) {
            loadingDialog.startLoadingDialog();
        }
    }


}