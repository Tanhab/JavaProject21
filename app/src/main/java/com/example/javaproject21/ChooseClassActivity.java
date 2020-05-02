package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChooseClassActivity extends AppCompatActivity {
    private static final String TAG = "ChooseClassActivity";
    private CardView cardNewClassroom,cardJoinClass;
    ImageButton btnLogout;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);
        cardNewClassroom=findViewById(R.id.cardCrateAClassroom);
        cardJoinClass=findViewById(R.id.cardJoinClassroom);
        btnLogout=findViewById(R.id.btnLogout);
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);

        cardNewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateClassDialog();

            }
        });
        cardJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog();

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Utils.setCR(null);
        Utils.setClassName(null);
        Utils.setUserName(null);
        Utils.setCR2(null);

        Utils.setImageUri("empty");
        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    void showCreateClassDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_classroom_dialog, null);

        Button acceptButton = view.findViewById(R.id.btnAddNewClassroom);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtClassroomName,edtDesc;
        final EditText edtUniName,edtInvitationCode;
        edtClassroomName=view.findViewById(R.id.edtClassroomName);
        edtDesc=view.findViewById(R.id.txtClassroomDesc);
        edtUniName=view.findViewById(R.id.edtUniName);
        edtInvitationCode=view.findViewById(R.id.edtInvitationCode);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();



        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if(edtClassroomName.getText().toString().length()<1)
                {
                    edtClassroomName.setError("Can't be empty");
                }else if(edtUniName.getText().toString().length()<1)
                {
                    edtUniName.setError("Can't be empty");
                }else if(edtInvitationCode.getText().toString().length()<1)
                {
                    edtInvitationCode.setError("Can't be empty");
                }else
                {

                    showConfirmDialog(edtClassroomName,edtInvitationCode,edtUniName,edtDesc);
                    alertDialog.dismiss();



                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: cancel button" );
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void showConfirmDialog(EditText edtClassroomName, EditText edtInvitationCode,EditText edtUniName,EditText edtDesc) {

        LayoutInflater inflater1 = LayoutInflater.from(this);
        View view1 = inflater1.inflate(R.layout.add_classroom_confirmity_dialog, null);

        Button YesButton = view1.findViewById(R.id.btnYES);
        Button NoButton = view1.findViewById(R.id.btnNO);

        final TextView txtVClassname,txtVCode,txtVDes;

        txtVClassname = view1.findViewById(R.id.txtClassName);
        txtVCode = view1.findViewById(R.id.txtInvitationCode);
        txtVDes = view1.findViewById(R.id.txtDesc);

        String classroomName;
        final String className,classDescription,uniName,invitationCode;
        classroomName=edtClassroomName.getText().toString().trim();
        classDescription=edtDesc.getText().toString().trim();
        uniName=edtUniName.getText().toString().trim().toUpperCase();
        invitationCode=edtInvitationCode.getText().toString().trim();
        className= classroomName+"_"+uniName;

        txtVClassname.setText("Classroom name : " +className);
        txtVCode.setText("Invitation Code : " + invitationCode);
        txtVDes.setText("Description : "+classDescription);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view1)
                .create();

        YesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                checkForName(className,invitationCode,classDescription);
                Log.d(TAG, "onClick: started final text= "+invitationCode);
                alertDialog.dismiss();
            }
        });

        NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showJoinDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.join_classroom_dialog, null);

        Button acceptButton = view.findViewById(R.id.btnJoin);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtClassroomName, edtInvitationcode;

        edtClassroomName = view.findViewById(R.id.edtClassroomName);
        edtInvitationcode = view.findViewById(R.id.edtInvitationCode);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if (edtClassroomName.getText().toString().length() < 1) {
                    edtClassroomName.setError("Can't be empty");

                    edtClassroomName.setFocusable(true);
                } else if (edtInvitationcode.getText().toString().length() < 1) {
                    edtInvitationcode.setError("Can't be empty");
                    edtInvitationcode.setFocusable(true);
                }else
                {
                    String className = edtClassroomName.getText().toString().trim();
                    String code = edtInvitationcode.getText().toString().trim();
                    pd.show();
                    Log.d(TAG, "onClick: started final text= " +className +" "+ code);
                    alertDialog.dismiss();
                    findClassroom(className,code);
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

    private void findClassroom(final String className, final String code) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Classrooms").document(className);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        checkForInvitationCode(className,code);
                    } else {
                        Log.d(TAG, "No such document");
                        pd.dismiss();
                        Toast.makeText(ChooseClassActivity.this, "No such class exists.Please enter a valid class name or create one.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    pd.dismiss();
                    Toast.makeText(ChooseClassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkForInvitationCode(final String className, final String code) {
        //DocumentReference docRef = FirebaseFirestore.getInstance().collection(className).document("classroomDetails");
        DocumentReference docRef= FirebaseFirestore.getInstance().collection("Classrooms").document(className);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String ckCode=document.getData().get("invitationCode").toString();
                        Log.d(TAG, "onComplete: ckcode = "+ckCode);
                        if(ckCode.equals(code))
                        {
                            addClassToUserDatabase(className);
                        }else{
                            pd.dismiss();
                            Toast.makeText(ChooseClassActivity.this, "Wrong invitation code.Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        pd.dismiss();
                        Log.d(TAG, "No such document");
                        Toast.makeText(ChooseClassActivity.this, "Joining failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    pd.dismiss();
                    Toast.makeText(ChooseClassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addClassToUserDatabase(final String className) {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object>map=new HashMap<>();
        map.put("currentClass",className);
        FirebaseFirestore.getInstance().collection("Users").document(email)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Utils.setClassName(className);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                e.printStackTrace();
                Toast.makeText(ChooseClassActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void checkForName(final String className, final String invitationCode, final String classDescription) {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Classrooms").document(className);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        pd.dismiss();
                        Toast.makeText(ChooseClassActivity.this, className+" already exists.Try another name", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "No such document");
                        createClassroom(className,invitationCode,classDescription);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    pd.dismiss();
                    Toast.makeText(ChooseClassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createClassroom(final String className, final String invitationCode, final String classDescription) {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Classrooms").document(className);
        Map<String ,Object> map= new HashMap<>();
        map.put("classroomName",className);
        docRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createDatabase(className,invitationCode,classDescription);
                
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                e.printStackTrace();
                Toast.makeText(ChooseClassActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                
            }
        });
    }

    private void createDatabase(final String className, final String invitationCode, final String classDescription) {
        Map<String,Object>map=new HashMap<>();
        map.put("invitationCode",invitationCode);
        map.put("description",classDescription);
        map.put("currentCR",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        map.put("currentCR2","n/a");
        //changed
        FirebaseFirestore.getInstance().collection("Classrooms").document(className).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: database updated");
                        addClassToUserDatabase(className);
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                e.printStackTrace();
                Toast.makeText(ChooseClassActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(Utils.getClassName()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        pd.show();
        checkforClassroom();*/

    }

    private void checkforClassroom() {
        String email =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String name=document.getData().get("currentClass").toString();
                        if(name.equals("empty")){
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }

                    } else {
                        pd.dismiss();
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    pd.dismiss();
                    Toast.makeText(ChooseClassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
