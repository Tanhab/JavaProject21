package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MyClassroomActivity extends AppCompatActivity {
    private static final String TAG = "MyClassroomActivity";

    private CardView cardLeave,cardClassInfo,cardClassmates,cardTeachers;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classroom);
        cardClassInfo=findViewById(R.id.cardClassInfo);
        cardClassmates=findViewById(R.id.cardClassmates);
        cardLeave=findViewById(R.id.cardLeave);
        cardTeachers=findViewById(R.id.cardTeachers);
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TeacherCourseActivity.class));
            }
        });
        cardLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLeave();

            }
        });
        cardClassmates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentsListSearchActivity.class));


            }
        });
        cardClassInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

    }

    private void confirmLeave() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.leave_confirmity_dialog, null);

        Button yesButton = view.findViewById(R.id.btnYes);
        Button noButton = view.findViewById(R.id.btnNo);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if(Utils.getCR().equals(email)||Utils.getCR2().equals(email)){
                    showCrChangeDialog();
                }else
                unsubscribeToClass();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showCrChangeDialog() {
        final AlertDialog alertDialog;
        alertDialog=new AlertDialog.Builder(this)
                .setTitle("Caution")
                .setMessage("Sorry,you can not leave classroom as long as you are a Class representative.Pass the duty to others then leave classroom.")
                .setPositiveButton("Goto CR Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),CrSettingsActivity.class));
                        dialog.dismiss();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.classroom_info_dialog, null);

        Button acceptButton = view.findViewById(R.id.btnJoin);

        final TextView txtClassname,txtCurrentCR,txtCode,txtDesc;

        txtClassname = view.findViewById(R.id.txtClassName);
        txtCode = view.findViewById(R.id.txtInvitationCode);
        txtCurrentCR = view.findViewById(R.id.txtCurrentCr);
        txtDesc = view.findViewById(R.id.txtDesc);

        txtClassname.setText(Utils.getClassName());
        txtCode.setText("Invitation Code : "+Utils.getInvitationCode());
        txtCurrentCR.setText("CurrentCR : "+Utils.getCR());
        txtDesc.setText("Description : "+Utils.getClassDescription());

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            alertDialog.dismiss();

            }
        });

        alertDialog.show();


    }

    private void clearClassnameFromDatabase() {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map=new HashMap<>();
        map.put("currentClass","empty");
        FirebaseFirestore.getInstance().collection("Users").document(email)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Utils.setClassName(null);
                Utils.setCR(null);
                Utils.setClassDescription(null);
                Utils.setCR2(null);
                Utils.setInvitationCode(null);
                Intent intent=new Intent(getApplicationContext(),ChooseClassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void unsubscribeToClass(){
        String topic=Utils.getTopic();
        Log.d(TAG, "unsubscribeToClass: topic "+ topic);

        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "unSubscribe to "+Utils.getTopic()+" done";
                        Utils.setTopic("");
                        clearClassnameFromDatabase();

                        if (!task.isSuccessful()) {

                            msg = "unSubscription to cse18 failed";
                        }
                        Log.d(TAG, msg);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
