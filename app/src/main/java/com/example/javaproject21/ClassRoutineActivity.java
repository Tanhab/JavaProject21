package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClassRoutineActivity extends AppCompatActivity {
    private static final String TAG = "ClassRoutineActivity";
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference classRoutineRef=db.collection("Classrooms").document(Utils.getClassName()).collection("ClassRoutines");///change kora lagbe
    private ClassRoutineAdapter classRoutineAdapter;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_routine);
        recyclerView=findViewById(R.id.routineRecView);
        setupRecView();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setupRecView() {
        Query query= classRoutineRef.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ClassRoutine> options= new FirestoreRecyclerOptions.Builder<ClassRoutine>()
                .setQuery(query,ClassRoutine.class)
                .build();
        classRoutineAdapter= new ClassRoutineAdapter(options);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classRoutineAdapter);
        Log.d(TAG, "setupRecView: number of elements = "+classRoutineAdapter.getItemCount());
        classRoutineAdapter.setOnItemClickListener(new ClassRoutineAdapter.ClassListener() {
            @Override
            public void handleStudent(final DocumentSnapshot snapshot) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClassRoutineActivity.this);
                alertDialogBuilder.setTitle("Delete this Document").setMessage("Are you sure ?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        snapshot.getReference().delete().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        classRoutineAdapter.notifyDataSetChanged();
                    }
                }).show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        classRoutineAdapter.notifyDataSetChanged();
        classRoutineAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        classRoutineAdapter.stopListening();
    }
}
