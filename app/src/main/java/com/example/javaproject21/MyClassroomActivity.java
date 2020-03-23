package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyClassroomActivity extends AppCompatActivity {
    private static final String TAG = "MyClassroomActivity";

    private CardView cardLeave,cardClassInfo,cardClassmates,cardTeachers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classroom);
        cardClassInfo=findViewById(R.id.cardClassInfo);
        cardClassmates=findViewById(R.id.cardClassmates);
        cardLeave=findViewById(R.id.cardLeave);
        cardTeachers=findViewById(R.id.cardTeachers);
        cardTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cardLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearClassnameFromDatabase();

            }
        });
        cardClassmates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentsListActivity.class));


            }
        });
        cardClassInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

    }

    private void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyClassroomActivity.this);
        String text="Class name : "+Utils.getClassName()+"\nInvitation Code : " +Utils.getInvitationCode()+"\nCurrent CR : "+Utils.getCR()+" \nClass Description : "+Utils.getClassDescription();
        alertDialogBuilder.setTitle("Classroom Information").setMessage(text).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
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
                startActivity(new Intent(getApplicationContext(),ChooseClassActivity.class));
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
}
