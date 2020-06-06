package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * The class for Cr settings activity.
 */
public class CrSettingsActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "CrSettingsActivity";
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;
    /**
     * The StudentAdapter object.
     */
    StudentAdapter adapter;
    /**
     * The ImageButton for back.
     */
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_settings);
        recyclerView=findViewById(R.id.recyclerView);
        initRecView();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * This method sets the adapter for the recycler view of the students under the classroom.
     */
private void initRecView() {
        Query query= FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass",Utils.getClassName())
                ;
        FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class)
                .build();
        adapter= new StudentAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new StudentAdapter.StudentListener() {
            @Override
            public void handleStudent(DocumentSnapshot snapshot) {
                ChangeCRDialog(snapshot);
            }
        });
    }

    /**
     * This method creates the alert dialog containing option of adding another CR and
     * making another one take his place of CRship.
     *
     * @param snapshot the snapshot
     */
private void ChangeCRDialog(final DocumentSnapshot snapshot) {
        String[]  listItems;
        boolean ck=false;
        Log.d(TAG, "ChangeCRDialog: cr2 = " +Utils.getCR2());
        if(Utils.getCR2().equals("n/a")){
        listItems = new String[]{"Make him/her other CR","Make him/her CR and leave CRship."};

        }else{
            ck=true;
            listItems= new String[]{"Make him/her CR and leave CRship."};
        }
        final AlertDialog.Builder builder= new AlertDialog.Builder(CrSettingsActivity.this)
                .setTitle("Choose an option")
                .setIcon(R.drawable.classroom);
        AlertDialog dialog= builder.create();
        final boolean finalCk = ck;
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    DoOperation(which,snapshot, finalCk);
                    dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
         dialog= builder.create();
        dialog.show();

    }

    /**
     * This method checks whether it is possible to add another CR and on vacancy in place of
     * CR1 or CR2 ,the selected student is allowed to be added in case of first option.
     * In case of second option, the current CR is removed from the database and in its place
     * the new assigned student is updated as new CR.
     *
     * @param which    the number of the option
     * @param snapshot the snapshot of the student
     * @param finalCk  the boolean for checking if CR2 is null or not
     */
//private
    void DoOperation(int which, DocumentSnapshot snapshot, boolean finalCk) {
        final Student student= snapshot.toObject(Student.class);
        if(which==0&& !finalCk)
        {
            if(!Utils.getCR2().equals("n/a")&& !Utils.getCR().equals("n/a")){
                Toast.makeText(this, "Sorry,only two Class Representative is allowed", Toast.LENGTH_SHORT).show();

            }else{
                if(Utils.getCR().equals("n/a")){
                    assert student != null;
                    addCR1(student);
                }else{
                    assert student != null;
                    addCR2(student);
                }
            }

        }else if(which==1){
            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();

            Map<String,Object>map= new HashMap<>();
           if(Utils.getCR().equals(email)) {
               assert student != null;
               map.put("currentCR",student.getEmail());
               Utils.setCR(student.getEmail());
           }
           else {
               assert student != null;
               map.put("currentCR2",student.getEmail());
               Utils.setCR2(student.getEmail());
           }

            FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(CrSettingsActivity.this, "Class Representative added.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CrSettingsActivity.this, "CR changing Failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            });


        }else if(which==0&& finalCk){
            String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();

            Map<String,Object>map= new HashMap<>();
            if(Utils.getCR().equals(email)) {
                assert student != null;
                map.put("currentCR",student.getEmail());
                Utils.setCR(student.getEmail());

            }
            else {
                assert student != null;
                map.put("currentCR2",student.getEmail());
                Utils.setCR2(student.getEmail());
            }

            FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(CrSettingsActivity.this, "Class Representative added."+Utils.getCR(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CrSettingsActivity.this, "CR changing Failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * This method adds the selected student as CR1.
     *
     * @param student the selected student to be CR1
     */
private void addCR1(Student student) {
        Map<String,Object>map= new HashMap<>();
        map.put("currentCR",student.getEmail());
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                .update(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(CrSettingsActivity.this, "CR changing Failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CrSettingsActivity.this, "Class Representative added.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * This method adds the selected student as CR2.
     *
     * @param student the selected student to be CR2
     */
    private void addCR2(final Student student) {
        Map<String,Object>map= new HashMap<>();
        map.put("currentCR2",student.getEmail());
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                .update(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(CrSettingsActivity.this, "CR changing Failed.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Utils.setCR2(student.getEmail());
                Toast.makeText(CrSettingsActivity.this, "Class Representative added."+Utils.getCR2(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: cr = "+ Utils.getCR2());

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
