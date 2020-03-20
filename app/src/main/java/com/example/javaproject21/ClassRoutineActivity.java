package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClassRoutineActivity extends AppCompatActivity {
    private static final String TAG = "ClassRoutineActivity";
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference classRoutineRef=db.collection("cse18");///change kora lagbe
    private ClassRoutineAdapter classRoutineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_routine);
        recyclerView=findViewById(R.id.routineRecView);

        setupRecView();


    }

    private void setupRecView() {
        Query query= classRoutineRef.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ClassRoutine> options= new FirestoreRecyclerOptions.Builder<ClassRoutine>()
                .setQuery(query,ClassRoutine.class)
                .build();
        classRoutineAdapter= new ClassRoutineAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classRoutineAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        classRoutineAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        classRoutineAdapter.stopListening();
    }
}
