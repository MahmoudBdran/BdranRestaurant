package com.example.bdranrestaurant.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdranrestaurant.AdminPackage.AdminActivity;
import com.example.bdranrestaurant.MainScreen.MainActivity;
import com.example.bdranrestaurant.R;
import com.example.bdranrestaurant.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
     TextView signUp_tv;
     EditText username_edt,password_edt;
     Button login_btn;
     ProgressDialog loginProgress;
     RelativeLayout Login_root_layout;

    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        flatenningComponents();
        login_btn.setOnClickListener(this);
        signUp_tv.setOnClickListener(this);
        Login_root_layout=findViewById(R.id.Login_root_layout);

    }
    private void flatenningComponents(){
        signUp_tv=findViewById(R.id.signup_txtv);
        username_edt=findViewById(R.id.email_edt);
        password_edt=findViewById(R.id.password_edt);
        login_btn=findViewById(R.id.signin_btn);
        loginProgress =new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_btn:
                String email=username_edt.getText().toString();
                String password=password_edt.getText().toString();
                if ( !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
                    loginProgress.setTitle("Logging in");
                    loginProgress.setMessage("please wait while we check your credentials.");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    login(email,password);
                }
                if (TextUtils.isEmpty(email)){
                    username_edt.setError("Enter email");
                }
                if (TextUtils.isEmpty(password)){
                    password_edt.setError("Enter password");
                }
                break;

            case R.id.signup_txtv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }


    //login method
    private void login(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (email.equals("admin@admin.com")&&password.equals("adminadmin")){
                        loginProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "Hello admin", Toast.LENGTH_SHORT).show();
                        FirebaseUser user =mAuth.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();

                    }else{
                        loginProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();


                        FirebaseUser user =mAuth.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                }else{
                    loginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Can't sign in. please check in the form and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
