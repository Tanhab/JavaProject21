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

/**
 * The class for choosing class activity.
 */
public class ChooseClassActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "ChooseClassActivity";
    /**
     * The CardView type variable for new classroom.
     */
private CardView cardNewClassroom;
    /**
     * The CardView type variable for join class.
     */
private CardView cardJoinClass;
    /**
     * The ImageButton for logout.
     */
    ImageButton btnLogout;
    /**
     * The ProgressDialog variable.
     */
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

    /**
     * This method handles the logging out from the page.
     */
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

    /**
     * This method creates the alertDialog for creating a new classroom
     * where various data are given input to and sends to the confirm
     * alert dialog if accept button is pressed.
     */
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

    /**
     * This method creates the alertDialog for confirming if the user wants to create
     * the class with the data he has given as input. If the yes button is pressed
     * then the class name is sent for checking.
     *
     * @param edtClassroomName  the classroom name
     * @param edtInvitationCode the invitation code of the classroom
     * @param edtUniName        the university name
     * @param edtDesc           the description of the classroom
     */
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

    /**
     * This method creates the alertDialog for joining a classroom
     * where classroom name and invitation code are given as input and sends
     * the data to find the classroom.
     */
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

    /**
     * This method searches for the class in the database.
     * If exists it then sends for checking the invitation code otherwise
     * a toast appears telling the unavailability of the class.
     *
     * @param className the name of the classroom
     * @param code      the invitation code of the classroom
     */
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

    /**
     * This method matches the invitation code of the joining class in the database.
     * If the code matches then sends for checking the invitation code otherwise
     * a toast appears telling the unavailability of the class.
     *
     * @param className the name of the class
     * @param code      the invitation of the class
     */
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

    /**
     * This method adds class to the user database.
     *
     * @param className the name of the class
     */
//private
    void addClassToUserDatabase(final String className) {
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

    /**
     * This method checks whether a class already exits with the same classname.
     *If such doesn't exit then the information are sent for creating a new
     * class otherwise a toast is shown telling the inconvenience.
     *
     * @param className        the name of the class
     * @param invitationCode   the invitation code of the class
     * @param classDescription the class description
     */
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

    /**
     * This method creates class taking in the class name,invitation code
     * and class description.
     *
     * @param className        the class name
     * @param invitationCode   the invitation code
     * @param classDescription the class description
     */
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

    /**
     * This method adds the class to the database making the user who created the
     * class as first CR and the CR2 remains null.
     *
     * @param className        the class name
     * @param invitationCode   the invitation code
     * @param classDescription the class description
     */
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

    /**
     * This method checks the current class of the user from the database
     * and sends to the main activity of the current class.
     */
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
