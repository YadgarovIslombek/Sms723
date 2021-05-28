package com.aid.sms723.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aid.sms723.R;

public class InfoActivity extends AppCompatActivity {
    TextView textView,toolbartxt,txtV1;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        textView = findViewById(R.id.textView3);
        txtV1 = findViewById(R.id.txtV1);
        Toolbar toolbarr = findViewById(R.id.toolbar);
        toolbartxt = toolbarr.findViewById(R.id.toolbar_title);
        toolbartxt.setText("Dastur haqida");
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "Telegram orqali bog'lanish  ");
        spanTxt.append("uchun bosing!");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/ID221423A"));
                startActivity(browserIntent);
            }
        }, spanTxt.length() - "privacy policy".length(), spanTxt.length(), 0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanTxt, TextView.BufferType.NORMAL);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_enter,R.anim.left_out);
    }

}