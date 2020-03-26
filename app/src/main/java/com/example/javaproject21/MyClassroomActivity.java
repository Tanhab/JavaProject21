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
import android.widget.TextView;
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
