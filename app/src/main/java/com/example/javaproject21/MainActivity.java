package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

            }
        });
        if (getIntent().hasExtra("category")){
           String name=getIntent().getStringExtra("category");
            Log.d(TAG, "onCreate: category = "+name);
           if(name.equals("classRoutine")) {
               Intent intent = new Intent(getApplicationContext(), ClassRoutineActivity.class);
               startActivity(intent);
           }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribeToClass();


    }
    private void subscribeToClass(){
        FirebaseMessaging.getInstance().subscribeToTopic("cse18")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribe to cse18 done";
                        if (!task.isSuccessful()) {
                            msg = "Subscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
