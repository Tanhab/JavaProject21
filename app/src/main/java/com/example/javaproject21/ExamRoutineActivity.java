package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * The class for Exam routine activity.
 */
public class ExamRoutineActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "ExamRoutineActivity";
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;
    /**
     * The ImageButton for back.
     */
private ImageButton btnBack;
    /**
     * The FirebaseFirestore variable.
     */
private FirebaseFirestore db= FirebaseFirestore.getInstance();
    /**
     * The CollectionReference variable.
     */
private CollectionReference ref= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("ExamRoutine");
    /**
     * The object of ExamRoutineAdapter.
     */
private ExamRoutineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_routine);
        recyclerView=findViewById(R.id.examRecView);
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
     * This method attaches the adapter to the recycler view and
     * on click of a item, an alert dialog appears whether to delete this or not.
     * If the delete button is pressed then the routine is deleted.
     */
private void setupRecView() {
        Query query= ref.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ExamRoutine> options= new FirestoreRecyclerOptions.Builder<ExamRoutine>()
                .setQuery(query,ExamRoutine.class)
                .build();
        adapter= new ExamRoutineAdapter(options);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "setupRecView: number of rec view "+adapter.getItemCount());
        adapter.setOnItemClickListener(new ExamRoutineAdapter.ExamListener() {
            @Override
            public void handleStudent(final DocumentSnapshot snapshot) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExamRoutineActivity.this);
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
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
