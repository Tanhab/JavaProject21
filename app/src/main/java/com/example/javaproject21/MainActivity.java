package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FloatingActionButton btnLogout;
    private CardView cardClassRoutine,cardExamRoutine,cardNotification,cardResources,cardCR,cardProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout= findViewById(R.id.btnLogout);
        cardClassRoutine=findViewById(R.id.cardClassRoutine);
        cardCR=findViewById(R.id.cardCR);
        cardExamRoutine=findViewById(R.id.cardExamRoutine);
        cardNotification=findViewById(R.id.cardNotification);
        cardResources=findViewById(R.id.cardResources);
        cardProfile=findViewById(R.id.cardProfile);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Utils.setCR(null);
                Utils.setClassName(null);
                Utils.setUserName(null);
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
        cardClassRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ClassRoutineActivity.class));

            }
        });
        cardExamRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ExamRoutineActivity.class));

            }
        });
        cardResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResourcesActivity.class));
            }
        });
        cardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NotificationActivity.class));

            }
        });
        cardCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CRActivity.class));

            }
        });
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyClassroomActivity.class));

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
      if(Utils.getClassName()==null) {
          checkForClassName();
      }else
      {
          checkForCR();
      }



    }

    private void checkForClassName() {

            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String className=document.getData().get("currentClass").toString();
                            if(className.equals("empty")){
                                startActivity(new Intent(getApplicationContext(),ChooseClassActivity.class));
                                finish();
                            }else {
                                Utils.setClassName(className);
                                String name = document.getData().get("name").toString();
                                Utils.setUserName(name);
                                checkForCR();
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                       if(getConnectionType(MainActivity.this)==0){
                           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                           alertDialogBuilder.setTitle("No Internet").setMessage("Please connect to internet for full functionality")
                                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int which) {
                                           dialog.dismiss();
                                       }
                                   }).show();
                       }
                        checkForClassName();
                    }
                }
            });

    }

    private void checkForCR() {
        Log.d(TAG, "checkForCR: started");
        if(Utils.getCR()==null){

            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            DocumentReference docRef = FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("classroomDetails");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String CRName=document.getData().get("currentCR").toString();
                            Utils.setCR(CRName);
                            subscribeToClass();
                        } else {
                            Log.d(TAG, "No such document");
                            checkForCR();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        }



    private void subscribeToClass(){
        FirebaseMessaging.getInstance().subscribeToTopic(Utils.getClassName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribe to "+Utils.getClassName()+" done";
                        if (!task.isSuccessful()) {
                            msg = "Subscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @IntRange(from = 0, to = 2)
    public static int getConnectionType(Context context) {
        int result = 0; // Returns connection type. 0: none; 1: mobile data; 2: wifi
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = 2;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = 1;
                    }
                }
            }
        }
        return result;
    }
}
