package com.aid.sms723.ui.activitys;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.aid.sms723.R;
import com.aid.sms723.model.UserData;
import com.aid.sms723.util.LocalStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    MaterialEditText _emailText;
    MaterialEditText _passwordText;
    AppCompatButton _loginButton;
    ProgressBar progressBar;
    TextView link_forgotpassword;
    LinearLayout _signupLink;
    FirebaseAuth auth;
    String lastName = "";
    String firstName= "";
    LocalStorage localStorage;
    private static final int REQUEST_SIGNUP = 0;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth =FirebaseAuth.getInstance();
        _emailText = findViewById(R.id.email);
        _passwordText = findViewById(R.id.password);
        _loginButton = findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbarL);
        _signupLink = findViewById(R.id.link_signup);
        link_forgotpassword = findViewById(R.id.link_forgotpassword);
        link_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                finish();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                finish();

                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = _emailText.getText().toString().trim();
                String password = _passwordText.getText().toString().trim();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Maydonlarni toldiring", Toast.LENGTH_SHORT).show();

                }else{
                    login(email,password);
                    localStorage = new LocalStorage(getApplicationContext());
                    localStorage.savePassword(password);
                }
                //login1();
            }
        });


    }


//   public void login1() {
//       Log.d(TAG, "Login");
//
//       if (!validate()) {
//           onLoginFailed();
//           return;
//       }
////       _loginButton.setEnabled(false);
//       progressBar.setVisibility(View.VISIBLE);
//       String email = _emailText.getText().toString().trim();
//       String password = _passwordText.getText().toString().trim();
//       auth.signInWithEmailAndPassword(email, password)
//               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                   @Override
//                   public void onComplete(@NonNull Task<AuthResult> task) {
//                       if (task.isSuccessful()) {
//                           // Sign in success, update UI with the signed-in user's information
//                           Log.d(TAG, "signInWithEmail:success");
//                           FirebaseUser user = auth.getCurrentUser();
//                           progressBar.setVisibility(View.GONE);
//                           updateUI(user);
//                       } else {
//                           // If sign in fails, display a message to the user.
//                           Log.w(TAG, "signInWithEmail:failure", task.getException());
//                           Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                   Toast.LENGTH_SHORT).show();
//                           progressBar.setVisibility(View.GONE);
//                           updateUI(null);
//                       }
//                   }
//               });
//       new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//           @Override
//           public void run() {
//// On complete call either onLoginSuccess or onLoginFailed
//               // onLoginSuccess();
//               // onLoginFailed();
//               progressBar.setVisibility(View.GONE);
//           }
//       },3000);
//   }
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//
//            /*so we have succesfully logged in the user */
//            Log.e(TAG,"we are in update UI");
//
//        } else {
//
//        }
//    }
//    private String getfirstname(String name) {
//
//        if(name.split("\\w+").length>1){
//
//            lastName = name.substring(name.lastIndexOf(" ")+1);
//            firstName = name.substring(0, name.lastIndexOf(' '));
//            return firstName;
//        }
//        else{
//            firstName = name;
//            return firstName;
//        }
//    }
//    private boolean havelastname(String name) {
//
//        if(name.split("\\w+").length>1){
//
//            lastName = name.substring(name.lastIndexOf(" ")+1);
//            firstName = name.substring(0, name.lastIndexOf(' '));
//            return true;
//        }
//        else{
//            firstName = name;
//            return false;
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }
//    public void onLoginSuccess() {
////        _loginButton.setEnabled(true);
//        finish();
//    }
//    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//
////        _loginButton.setEnabled(true);
//    }
//    public boolean validate() {
//        boolean valid = true;
//
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
//
//        return valid;
//    }
//
    private void login(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(task.isSuccessful()){

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }else{
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
  //  @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        progressBar.setVisibility(View.GONE);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivityStudent
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

    }
}
