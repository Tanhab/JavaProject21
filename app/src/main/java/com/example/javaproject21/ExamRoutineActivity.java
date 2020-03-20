package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ExamRoutineActivity extends AppCompatActivity {
    private static final String TAG = "ExamRoutineActivity";
    RecyclerView recyclerView;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference ref= db.collection("cse18").document("ExamRoutines").collection("ExamRoutines");
    private ExamRoutineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_routine);
        recyclerView=findViewById(R.id.examRecView);
        setupRecView();

    }

    private void setupRecView() {
        Query query= ref.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ExamRoutine> options= new FirestoreRecyclerOptions.Builder<ExamRoutine>()
                .setQuery(query,ExamRoutine.class)
                .build();
        adapter= new ExamRoutineAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
