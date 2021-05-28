package com.aid.sms723.ui.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.aid.sms723.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
public class ForLoopDialogFragment extends DialogFragment {
    private MaterialEditText kod;
    private MaterialEditText prefiks;
    private MaterialEditText startNum;
    private MaterialEditText endNum;
    String _startNum, _endNum;
    String _kod,_prefiks;
    ImageView imageView;
    ArrayList<String> listRaqam = new ArrayList<>();
    String raqam;
    ProgressBar progressBar;
    LinearLayout ll;
    public ForLoopDialogFragment() {
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
        View view = inflater.inflate(R.layout.fragment_for_loop_dialog, container, false);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        kod = view.findViewById(R.id.kod);
        prefiks = view.findViewById(R.id.prefiks);
        startNum = view.findViewById(R.id.startNum);
        endNum = view.findViewById(R.id.endNum);
        MaterialEditText kodstrana = view.findViewById(R.id.kodstrana);
        AppCompatButton btn_start = view.findViewById(R.id.btn_start);
        imageView = view.findViewById(R.id.imgView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar11);
        imageView.setOnClickListener(view12 -> Objects.requireNonNull(getDialog()).dismiss());
        ll = (LinearLayout) view.findViewById(R.id.consLayout);
        ll.setAlpha((float) 0.4);
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; ++i)
            {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                {
                    return "";
                }
            }

            return null;
        };

        prefiks.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(3)});
        kod.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(2)});
        kodstrana.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(0)});
        kodstrana.setEnabled(false);
        endNum.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(4)});
        startNum.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(4)});
        btn_start.setOnClickListener(view1 -> {
            StringBuilder sb=new StringBuilder();
            _kod = Objects.requireNonNull(kod.getText()).toString().trim();
            _prefiks = Objects.requireNonNull(prefiks.getText()).toString().trim();
            _startNum = (Objects.requireNonNull(startNum.getText()).toString()).trim();
            _endNum = (Objects.requireNonNull(endNum.getText()).toString()).trim();
            kod.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String edtChar=kod.getText().toString().trim();
                    if(edtChar.length()==2)
                    {
                        sb.append(edtChar);
                        prefiks.requestFocus();
                    }else if(edtChar.length()==0) {
                        sb.deleteCharAt(0);
                        kod.requestFocus();
                    }


                }
            });
            prefiks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String edtChar=prefiks.getText().toString().trim();
                    if(edtChar.length()==3)
                    {
                        sb.append(edtChar);

                        startNum.requestFocus();
                    }else if(edtChar.length()==0) {
                        sb.deleteCharAt(1);
                        prefiks.requestFocus();
                    }


                }
            });
            startNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String edtChar=startNum.getText().toString().trim();
                    if(edtChar.length()==4)
                    {
                        sb.append(edtChar);
                        endNum.requestFocus();
                    }else if(edtChar.length()==0) {
                        sb.deleteCharAt(2);
                        startNum.requestFocus();
                    }


                }
            });
            endNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String edtChar=endNum.getText().toString().trim();
                    if(edtChar.length()==4)
                    {
                        sb.append(edtChar);

                    }else if(edtChar.length()==0) {
                        sb.deleteCharAt(3);
                        endNum.requestFocus();
                    }


                }
            });
            if(TextUtils.isEmpty(_kod) || TextUtils.isEmpty(_prefiks) ||  TextUtils.isEmpty(_startNum) ||  TextUtils.isEmpty(_endNum) ){
                Toast.makeText(getContext(), "Maydonlarni to'ldiring", Toast.LENGTH_SHORT).show();
            }else if(kod.length()<2 ){
               kod.setError("To'ldiring");
                prefiks.requestFocus();
            }else if(prefiks.length()<3 ){
                prefiks.setError("To'ldiring");
                startNum.requestFocus();
            }else if(startNum.length()<4 ){
                startNum.setError("To'ldiring");
                endNum.requestFocus();
            }else if(endNum.length()<4 ){
                endNum.setError("To'ldiring");
                endNum.requestFocus();
            }
            else{
                int start, end;
                start = Integer.parseInt(_startNum);
                end = Integer.parseInt(_endNum);
                if(start>end){
                    endNum.setError(String.format("%04d", start) + " dan katta qiymat kiriting!");
                }else{

                forLoop(start,end);
                }

            }
        });
        return view;
    }
    public void forLoop(int start, int end) {
        ll.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                for (int i = start; i < end + 1; i++) {
                    String formatted = String.format("%04d", i);
                    raqam = "+998"+_kod + _prefiks + formatted;
                    listRaqam.add(raqam);
                }
                OnInputListener listener =  getListener();
                if(listener != null){
                    listener.sendInput(listRaqam);
                }

                    progressBar.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    Objects.requireNonNull(getDialog()).dismiss();

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        },2000);

    }
    public interface OnInputListener {
        void sendInput(ArrayList<String> input);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            onInputListener = (OnInputListener) getActivity();
//        } catch (ClassCastException e) {
//            Log.e(TAG, "onAttach: " + e.getMessage());
//        }
//    }
    private OnInputListener getListener(){
        OnInputListener listener;
        try{
            Fragment onInputSelected_Fragment = getTargetFragment();
            if (onInputSelected_Fragment != null){
                listener = (OnInputListener) onInputSelected_Fragment;
            }
            else {
                Activity onInputSelected_Activity = getActivity();
                listener = (OnInputListener) onInputSelected_Activity;
            }
            return listener;
        }catch(ClassCastException e){
            Log.e("Custom Dialog", "onAttach: ClassCastException: " + e.getMessage());
        }
        return null;
    }

}
