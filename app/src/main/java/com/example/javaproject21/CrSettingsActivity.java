package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
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

public class CrSettingsActivity extends AppCompatActivity {
    private static final String TAG = "CrSettingsActivity";
    RecyclerView recyclerView;
    StudentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_settings);
        recyclerView=findViewById(R.id.recyclerView);
        initRecView();

    }
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

    private void ChangeCRDialog(final DocumentSnapshot snapshot) {
        String[]  listItems= new String[]{"Make him/her other CR","Make him/her CR and leave CRship."};
        final AlertDialog.Builder builder= new AlertDialog.Builder(CrSettingsActivity.this)
                .setTitle("Choose an option")
                .setIcon(R.drawable.classroom);
        AlertDialog dialog= builder.create();
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    DoOperation(which,snapshot);
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

    private void DoOperation(int which, DocumentSnapshot snapshot) {
        Student student= snapshot.toObject(Student.class);
        if(which==0)
        {
            if(Utils.getCR2()!=null&& Utils.getCR()!=null){
                Toast.makeText(this, "Sorry,only two Class Representative is allowed", Toast.LENGTH_SHORT).show();

            }else{
                if(Utils.getCR()==null){
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
           }
           else {
               assert student != null;
               map.put("currentCR2",student.getEmail());
           }

            FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("classroomDetails").update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CrSettingsActivity.this, "Class Representative added.", Toast.LENGTH_SHORT).show();

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

    private void addCR1(Student student) {
        Map<String,Object>map= new HashMap<>();
        map.put("currentCR",student.getEmail());
        FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("classroomDetails")
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
    private void addCR2(Student student) {
        Map<String,Object>map= new HashMap<>();
        map.put("currentCR2",student.getEmail());
        FirebaseFirestore.getInstance().collection(Utils.getClassName()).document("classroomDetails")
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
