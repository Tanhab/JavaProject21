package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * The class for Timeline fragment.
 */
public class TimelineFragment extends Fragment {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "TimelineFragment";
    /**
     * The FirebaseAuth variable.
     */
private FirebaseAuth firebaseAuth;
    /**
     *  DocumentReference variable for class name.
     */
private DocumentReference firebaseFirestore= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName());
    /**
     * The Recycler view variable.
     */
private RecyclerView recyclerView;
    /**
     * The array list variable for Post list.
     */
private List<Post> postList;
    /**
     * The CollectionReference variable.
     */
private CollectionReference classRoutineRef;
    /**
     * The  FloatingActionButton variable.
     */
    FloatingActionButton fab;

    /**
     * The Post recycler adapter.
     */
    PostRecyclerAdapter postRecyclerAdapter;

    /**
     * Instantiates a new Timeline fragment.
     */
    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        classRoutineRef= firebaseFirestore.collection("Posts");
        recyclerView = view.findViewById(R.id.recyclerView);
        fab=view.findViewById(R.id.add_post_btn);
        setupRecView();


        firebaseAuth = FirebaseAuth.getInstance();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(),CreatePostActivity.class));
            }
        });


        return view;
    }

    /**
     * This method sets up the adapter to the recycler view of
     * the timeline fragment and on click of an item an alert dialog is
     * created on confirmation of deletion of the post.
     */
private void setupRecView() {
        Query query= classRoutineRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Post> options= new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query,Post.class)
                .build();
        postRecyclerAdapter= new PostRecyclerAdapter(options);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postRecyclerAdapter);
        postRecyclerAdapter.startListening();
        postRecyclerAdapter.setOnItemClickListener(new PostRecyclerAdapter.PostListener() {
            @Override
            public void handleStudent(final DocumentSnapshot snapshot) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
                        postRecyclerAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        postRecyclerAdapter.stopListening();
    }
}
