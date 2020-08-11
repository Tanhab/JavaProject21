package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * The class for Poll fragment.
 */
public class PollFragment extends Fragment {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "PollFragment";
    /**
     * The Recycler view variable.
     */
private RecyclerView pollRecView;
    /**
     * The object of PollRecyclerAdapter.
     */
private PollRecyclerAdapter pollRecyclerAdapter;
    /**
     * The FloatingActionButton variable.
     */
    FloatingActionButton fab;


    /**
     * Instantiates a new Poll fragment.
     */
    public PollFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_poll, container, false);
        pollRecView=view.findViewById(R.id.recyclerView);
        fab=view.findViewById(R.id.add_post_btn);


        Query query= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").orderBy("priority", Query.Direction.DESCENDING);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        FirestoreRecyclerOptions<Poll> options= new FirestoreRecyclerOptions.Builder<Poll>()
                .setQuery(query,Poll.class)
                .build();
        pollRecView.setLayoutManager(layoutManager);
        pollRecyclerAdapter=new PollRecyclerAdapter(options);

        pollRecView.setAdapter(pollRecyclerAdapter);
        pollRecyclerAdapter.startListening();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CreatePollActivity.class));

            }
        });
        pollRecyclerAdapter.setOnItemClickListener(new PollRecyclerAdapter.PollListener() {
            @Override
          public void handleStudent(final DocumentSnapshot snapshot) { AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
                       pollRecyclerAdapter.notifyDataSetChanged();
                    }
                }).show();
           }
       });





        return view;
    }

    /**
     * This method creates poll.
     */
private void cratePoll(){
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String msg="This is a poll.\nChoose the greatest fokinni .";
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a=YEAR*100+MONTH;
        a=a*100+DATE;
        a=a*100+HOUR;
        a=a*100+MINUTE;
        String time = DateFormat.format("h:mm a", calendar).toString();
        String date =DateFormat.format("dd.MM.yy", calendar).toString();
        final String pollId=email+ String.valueOf(a);
        date= time + " " + date;
        String uri="https://scontent.fdac12-1.fna.fbcdn.net/v/t1.15752-9/89257742_197521211473213_7312614522215202816_n.jpg?_nc_cat=110&_nc_sid=b96e70&_nc_eui2=AeEqaKjSMyB3uO1pNIPRAoub_mgegDv5LfT-aB6AO_kt9NgqUyz9r5rFAXW8nHUJdS-UcQuI9TuFhlcIqlEhbmWF&_nc_ohc=pCD-e6kdnEsAX8FV1bL&_nc_ht=scontent.fdac12-1.fna&oh=415c98ca2cbb7b0aed0d936efff03232&oe=5EC040CA";
        Poll poll=new Poll("Tanhab",msg,date,pollId,uri,a);

        final List<PollOption> pollOptions= new ArrayList<>();
        PollOption one = new PollOption("Dristy","Dristy",0);
        PollOption two = new PollOption("Preity","Preity",0);
        PollOption three = new PollOption("Nuzhat","Nuzhat",0);

        pollOptions.add(one);
        pollOptions.add(two);
        pollOptions.add(three);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document("PollId").set(poll)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updatePollOptions(pollOptions,pollId);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });




    }


    /**
     * This method updates poll options to the assigned poll.
     *
     * @param pollOptions the poll options
     * @param pollId      the poll id
     */
private void updatePollOptions(List<PollOption> pollOptions, String pollId) {
        for (PollOption p : pollOptions){
            FirebaseFirestore.getInstance().collection("Polls").document(pollId).collection("Options")
                    .document(p.getOptionId()).set(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(pollRecyclerAdapter!=null)pollRecyclerAdapter.stopListening();
    }
}
