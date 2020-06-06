package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * The class of CR activity.
 */
public class CRActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "CRActivity";
    /**
     * The CardView variable for create routine.
     */
private CardView cardCreateRoutine;
    /**
     * The CardView variable for create exam.
     */
private CardView cardCreateExam;
    /**
     * The CardView variable for change invitation code.
     */
private CardView cardChangeInvitationCode;
    /**
     * The CardView variable for class control.
     */
private CardView cardClassControl;
    /**
     * The CardView variable for cr settings.
     */
private CardView cardCRSettings;
    /**
     * The CardView variable for teacher course.
     */
private CardView cardTeacherCourse;
    /**
     * The ImageButton for back.
     */
private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r);
        cardCreateRoutine=findViewById(R.id.cardCreateClass);
        cardCreateExam=findViewById(R.id.cardCreateExam);
        cardChangeInvitationCode=findViewById(R.id.cardChangeCode);
        cardClassControl=findViewById(R.id.cardClassControl);
        cardCRSettings=findViewById(R.id.cardCRSettings);
        cardTeacherCourse=findViewById(R.id.cardCreateTeacherCourse);

        cardCreateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateExamRoutineActivity.class));
            }
        });
        cardCreateRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateClassRoutineActivity.class));
            }
        });
        cardTeacherCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateTeacherCourseActivity.class));
            }
        });
        cardChangeInvitationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCodeDialog();
            }
        });
        cardClassControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ClassControlActivity.class));
            }
        });
        cardCRSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CrSettingsActivity.class));
            }
        });
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * This method creates an alert dialog for giving the new invitation
     * code as input.If the accept button is pressed then change code
     * method is called.
     */
private void showCodeDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.change_code_dialog, null);

        Button acceptButton = view.findViewById(R.id.btnJoin);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtCode, confirmCode;

        edtCode = view.findViewById(R.id.edtInvitationCodeRow);
        confirmCode = view.findViewById(R.id.edtConfirmCode);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();



        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code=edtCode.getText().toString();
                final String ckCode=confirmCode.getText().toString();
                Log.e(TAG, "onClick: accept button");
                if (code.length() < 1) {
                    edtCode.setError("Can't be empty");
                    edtCode.setFocusable(true);
                } else if (!code.equals(ckCode)) {
                    confirmCode.setError("Doesn't match");
                    confirmCode.setFocusable(true);
                }else
                {

                    changeCode(code);
                    alertDialog.dismiss();

                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: cancel button");

                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }

    /**
     * This method changes the code of the classroom and update it to the database.
     *
     * @param code the new invitation code
     */
private void changeCode(String code) {
        Map<String ,Object> map=new HashMap<>();
        map.put("invitationCode",code);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CRActivity.this, "Invitation code changed.", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(CRActivity.this, "Code changing failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
