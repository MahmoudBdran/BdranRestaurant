package com.example.bdranrestaurant.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bdranrestaurant.MainScreen.MainActivity;
import com.example.bdranrestaurant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email_edt,password_edt,phone_edt,username_edt;
    private Button signup_btn;
    private ProgressDialog regProgress;
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference reference=database.getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerFlatteningComponents();
        signup_btn.setOnClickListener(this);
    }
    private void registerFlatteningComponents(){
        email_edt=findViewById(R.id.email_edt);
        password_edt=findViewById(R.id.password_edt);
        phone_edt=findViewById(R.id.phone_edt);
        signup_btn=findViewById(R.id.signup_btn);
        username_edt=findViewById(R.id.username);
        regProgress=new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_btn:
                String email=email_edt.getText().toString();
                String password=password_edt.getText().toString();
                String display_name=username_edt.getText().toString();
                String phone =phone_edt.getText().toString();
                if (!TextUtils.isEmpty(display_name)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
                    regProgress.setTitle("Registering User");
                    regProgress.setMessage("please wait while we create your account");
                    regProgress.setCanceledOnTouchOutside(false);
                    regProgress.show();
                    signUp(display_name,email,password,phone);
                }
                if(TextUtils.isEmpty(display_name)){
                    username_edt.setError("Enter Username");
                }
                if (TextUtils.isEmpty(email)){
                    email_edt.setError("Enter email");
                }
                if (TextUtils.isEmpty(password)){
                    password_edt.setError("Enter password");
                }
                if (TextUtils.isEmpty(phone)){
                    phone_edt.setError("Enter phone number");
                }
                break;
        }

    }
    private void signUp(final String display_name, final String email, String password, final String phone){


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                    String uid=user.getUid();
                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("name",display_name);
                    userMap.put("email",email);
                    userMap.put("phone",phone);

                    reference.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                regProgress.dismiss();
                                Toast.makeText(RegisterActivity.this, "account created successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    regProgress.hide();
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
