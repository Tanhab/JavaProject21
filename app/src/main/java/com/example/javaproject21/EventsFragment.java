package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * The class for Events fragment.
 */
public class EventsFragment extends Fragment {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "EventsFragment";
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;
    /**
     * The object of EventAdapter.
     */
    EventAdapter adapter;
    /**
     * The FloatingActionButton variable.
     */
    FloatingActionButton fab;

    /**
     * Instantiates a new Events fragment.
     */
    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        fab=view.findViewById(R.id.add_post_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CreateEventActivity.class));
            }
        });
        initRecView();
        return view;
    }

    /**
     * This method sets the adaptor to the recycler view of the events activity where
     * the newer events remain front by priority and on click delete button an alert dialog
     * is formed on confirming the deletion .
     */
private void initRecView() {
        Query query=         FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Events")
                .orderBy("priority",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Event> options=new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query,Event.class)
                .build();
        adapter= new EventAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.setOnItemClickListener(new EventAdapter.EventsListener() {
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
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
