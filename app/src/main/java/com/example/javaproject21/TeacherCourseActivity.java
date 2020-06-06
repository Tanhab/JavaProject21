package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * The class for Teacher course activity.
 */
public class TeacherCourseActivity extends AppCompatActivity {
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;

    /**
     * The FirebaseFirestore variable.
     */
private FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**
     * The CollectionReference variable.
     */
private CollectionReference teacherCourseRef=FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Teachers&courses");///change kora lagbe
    /**
     * The object of TeacherCourseAdapter.
     */
private TeacherCourseAdapter teacherCourseAdapter;
    /**
     * The ImageButton for back.
     */
private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course);
        recyclerView=findViewById(R.id.recyclerView);
        setupRecView();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /**
     * This method sets the adapter to the recycler view of the teacherCourseActivity
     * and priority has been set in such a way that the new ones will at the top.
     */
private void setupRecView() {
        Query query= teacherCourseRef.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<TeacherCourse> options= new FirestoreRecyclerOptions.Builder<TeacherCourse>()
                .setQuery(query,TeacherCourse.class)
                .build();
        teacherCourseAdapter = new TeacherCourseAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teacherCourseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        teacherCourseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        teacherCourseAdapter.stopListening();
    }


}
