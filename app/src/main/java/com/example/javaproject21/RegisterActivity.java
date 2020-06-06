package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * The class handles the register of a new user
 * and adding all the information to the database
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat
     */
private static final String TAG = "RegisterActivity";

    /**
     * EditText type variable for email.
     */
    EditText txtEmail;
    /**
     * EditText type variable for password.
     */
    EditText txtPassword;
    /**
     * EditText type variable for confirm password.
     */
    EditText txtConfirmPassword;
    /**
     * Button type variable for register.
     */
    Button btnRegister;
    /**
     * TextView type variable for login.
     */
    TextView txtLoginGo;
    /**
     * FirebaseAuth variable.
     */
private FirebaseAuth mAuth;
    /**
     * ProgressDialog variable.
     */
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail= findViewById(R.id.txtEmailInp);
        txtPassword=findViewById(R.id.txtPasswordInp);
        btnRegister=findViewById(R.id.btnRegister);
        txtLoginGo=findViewById(R.id.txtLoginAccount);
        txtConfirmPassword= findViewById(R.id.txtPasswordConfirm);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setTitle("Registering User...");
        pd.setCancelable(false);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"btnregister pressed");
                String email= txtEmail.getText().toString().trim();
                String password= txtPassword.getText().toString().trim();
                String confirmPassword= txtConfirmPassword.getText().toString().trim();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    txtEmail.setFocusable(true);
                    txtEmail.setError("Invalid Email Address");

                }else if(password.length()<6)
                {
                    txtPassword.setError("Length should at least be 6");
                    txtPassword.setFocusable(true);
                }else if(!password.equals(confirmPassword)) {
                    txtConfirmPassword.setFocusable(true);
                    txtConfirmPassword.setError("Password didn't match");
                }else
                {
                    pd.show();
                    RegisterUser(email,password);
                }
            }
        });
        txtLoginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }

    /**
     * This method handles the register of a new user
     * If the sign up is successful then the user info is been added
     * to the database otherwise a message is displayed of Authentication Failed
     *
     * @param email    the email address of the registered user
     * @param password the password of the registered user
     */
private void RegisterUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addtoDatabase();


                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.\nIf you already have an account with this email, please login.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    /**
     *This method updates the information of a new user signed in
     * to the database and takes the user to
     * the Profile Activity to update his profile
     */
private void addtoDatabase() {
        Log.d(TAG, "addtoDatabase: started");
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("Email",email);
        map.put("currentClass","empty");
        map.put("name","user");
        map.put("imageUri","empty");
        map.put("BloodGroup","N/A");
        map.put("PhoneNo","N/A");
        map.put("nickName","user");
        FirebaseFirestore.getInstance().collection("Users").document(email).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
