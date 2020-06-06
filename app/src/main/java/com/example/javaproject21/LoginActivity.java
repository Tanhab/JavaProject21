package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the login of a registered user,
 * recovery of forgot password and
 * google sign in
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat
     */
private  static final String TAG = "LoginActivity";

    /**
     * EditText type variable for email.
     */
private EditText txtEmail;
/**
     * EditText type variable for password.
     */
private EditText  txtPassword;
    /**
     * Button type variable for login.
     */
private Button btnLogin;
    /**
     * TextView type variable for register.
     */
private TextView txtReg;
/**
     * TextView type variable for forgot password.
     */
private TextView  txtForgotPassword;
    /**
     * FirebaseAuth variable.
     */
private FirebaseAuth mAuth;
    /**
     * Button type variable for google signin.
     */
    SignInButton btnGoogle;
    /**
     * GoogleSignInClient variable.
     */
    GoogleSignInClient googleSignInClient;
    /**
     * The request code for google signin
     */
 private int RC_SIGN_IN= 001;
    /**
     * ProgressDialog variable.
     */
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail= findViewById(R.id.txtEmailInput);
        txtPassword=findViewById(R.id.txtPasswordInput);
        btnLogin=findViewById(R.id.btnLogin);
        txtReg=findViewById(R.id.txtCreateAccount);
        mAuth = FirebaseAuth.getInstance();
        btnGoogle=findViewById(R.id.btnGoogle);
        txtForgotPassword=findViewById(R.id.txtForgotPassword);
        pd = new ProgressDialog(this);
        pd.setTitle("Logging in...");
        pd.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"btnlogin pressed");
                String email= txtEmail.getText().toString().trim();
                String password= txtPassword.getText().toString().trim();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    txtEmail.setFocusable(true);
                    txtEmail.setError("Invalid Email Address");

                }else if(password.length()<6)
                {
                    txtPassword.setError("Length should at least be 6");
                    txtPassword.setFocusable(true);
                }else {
                    pd.show();
                    loginUser(email,password);
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(this,gso);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });

        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));


            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotDialog();
            }
        });

    }

    /**
     * This method is used for showing an alert dialog where
     * a user will provide his email address to recover his password
     * of his account
     */
private void showForgotDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout= new LinearLayout(this);
        final EditText edtEmail = new EditText(this);
        edtEmail.setHint("Email");
        edtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtEmail.setMinEms(16);

        linearLayout.addView(edtEmail);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email= edtEmail.getText().toString().trim();
                beginRecovery(email);
            }
        });builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    /**
     * This method is used to recover the password of a user
     * @param email the email of the user needed to recover password
     */
 private  void beginRecovery(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Recovery Email Sent", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(LoginActivity.this, "Sending Email failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                pd.show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    /**
     * This method handles the signing in with google account
     *
     * @param acct get an ID token from the GoogleSignInAccount object,
     * exchange it for a Firebase credential, and authenticate with Firebase
     * using the Firebase credential
     */
private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                          if(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp()==
                                    FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp())
                          {
                              addtoDatabase();

                          }else{
                              pd.dismiss();
                              startActivity(new Intent(LoginActivity.this,MainActivity.class));
                              finish();
                          }


                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Sign in failed.Please check your internet connection. ", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    /**
     * This method handles the login of a user
     * If the authentication is successful then the user is brought to the main activity
     * otherwise a message is displayed of Authentication Failed
     *
     * @param email    the email address of the user
     * @param password the password of the user
     */
  private  void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            pd.dismiss();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    /**
     *This method updates the information of a new user signed in
     * with google signin to the database and takes the user to
     * the Profile Activity to update his account profile
     */
 private  void addtoDatabase() {
        Log.d(TAG, "addtoDatabase: started");
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("Email",email);
        map.put("currentClass","empty");
        map.put("name","user");
        map.put("imageUri","empty");
        map.put("BloodGroup","N/A");
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
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
