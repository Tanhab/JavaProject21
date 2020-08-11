package com.example.javaproject21;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * The class for Poll option recycler adapter.
 */
public class PollOptionRecAdapter extends FirestoreRecyclerAdapter<PollOption, PollOptionRecAdapter.PollOptionViewHolder> {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "PollOptionRecAdapter";
    /**
     * The Context variable.
     */
private Context context;
    /**
     * The ProgressDialog variable.
     */
    ProgressDialog pd;
    private RecyclerView recyclerView;
    private VoterRecAdapter voterRecAdapter;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions}* for configuration options.
     *
     * @param options the options
     */
    public PollOptionRecAdapter(@NonNull FirestoreRecyclerOptions<PollOption> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PollOptionViewHolder holder, int position, @NonNull PollOption model) {
        holder.optionName.setText(model.getPollName());
        holder.txtVoteCount.setText(String.valueOf(model.getCount())+" votes");
        final String optionId= model.getOptionId();
        final String pollId= model.getPollId();
        final String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final CollectionReference ref= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(pollId).collection("Votes");
        ref.whereEqualTo("email",email).addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {
                    e.printStackTrace();
                }

                if(!queryDocumentSnapshots.isEmpty())
                {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String gotId=doc.getDocument().getData().get("optionId").toString();
                            Log.d(TAG, "onEvent: paisi" + gotId);
                            if(gotId.equals(optionId)){
                                holder.checkBox.setImageDrawable(context.getDrawable(R.drawable.checked));
                            }


                        }

                        }

                }
            }
        });
        //ref e doc ache ?
        //yes-> oita ki same ? yes- > uncheck and delete and counter-- : No -> show dialog
        //No - > create data in ref .make check counter++


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                assert email != null;
                ref.document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d(TAG, "onComplete: document found "+document.toString());
                                String fetchedId=document.getData().get("optionId").toString();
                                if(fetchedId.equals(optionId)){
                                    Log.d(TAG, "onComplete: they are equal "+ optionId);
                                    //holder.checkBox.setImageDrawable(context.getDrawable(R.drawable.checked));
                                    deleteDoc(pollId,email);
                                    updateCounter(pollId,optionId,-1);
                                    pd.dismiss();



                                }else{
                                    Log.d(TAG, "onComplete: They are not equal");
                                    Toast.makeText(context, "You have already checked an option.\nPlease uncheck it then check another", Toast.LENGTH_LONG).show();
                                    pd.dismiss();

                                }
                            }else{
                                Log.d(TAG, "onComplete: document does not exist");
                                Log.d(TAG, "onComplete: creating new data ");
                                Map<String ,Object> map =new HashMap<>();
                                map.put("optionId",optionId);
                                map.put("email",email);
                                map.put("name",Utils.getUserName());
                                map.put("imageUri",Utils.getImageUri());

                                ref.document(email).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        holder.checkBox.setImageDrawable(context.getDrawable(R.drawable.unchecked));
                                        updateCounter(pollId,optionId,1);
                                        pd.dismiss();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        pd.dismiss();
                                    }
                                });

                            }


                        }else{
                            Log.d(TAG, "onComplete: eroor "+ task.getException());
                            pd.dismiss();
                        }
                    }
                });

            }
        });


        // Trying

        holder.txtVoteCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.poll_checkedlist_dialog, null);
                    ImageButton cancelButton = view.findViewById(R.id.closeBtnId);
                    RecyclerView recyclerView =view.findViewById(R.id.stdlistRecId);
                    recyclerView.setLayoutManager(new LinearLayoutManager( context));
                    Log.d(TAG, "onClick: " + pollId +   " " + optionId);

                    Query query= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                            .collection("Polls").document(pollId).collection("Votes")
                            .whereEqualTo("optionId",optionId);
                    FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                            .setQuery(query,Student.class)
                            .build();
                    voterRecAdapter = new VoterRecAdapter(options);
                    recyclerView.setAdapter(voterRecAdapter);
                    voterRecAdapter.startListening();

                final AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setView(view)
                        .create();


                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e(TAG, "onClick: cancel button" );
                            alertDialog.dismiss();
                        }
                    });

                alertDialog.show();


                }
                catch (Exception e){
                    Log.d(TAG, "onClick: " + e);

                }
            }

        });


       /* holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked())
                {
                    Log.d(TAG, "onClick: data checkbox is checked");
                    ref.document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data:CK  " + document.getData());
                                String fetchedId=document.getData().get("optionId").toString();
                                if(fetchedId.equals(optionId)){
                                    
                                    //holder.checkBox.setChecked(false);
                                }else{
                                    holder.checkBox.setChecked(false);
                                    
                                    
                                    //alertdialog


                                }
                            } else {
                                Log.d(TAG, "onComplete: creating new data ");
                                Map<String ,Object> map =new HashMap<>();
                                map.put("optionId",optionId);
                                map.put("email",email);

                                ref.document(email).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateCounter(pollId,optionId,1);
                                        holder.checkBox.setChecked(true);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                                


                                Log.d(TAG, "No such document");
                            }
                        } else {

                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

                }else{
                    Log.d(TAG, "onClick: ck is unchecked");
                    updateCounter(pollId,optionId,-1);
                    Log.d(TAG, "onComplete: data doc deleted");
                    deleteDoc(pollId,email);


                }
            }
        });*/




    }

    /**
     * This method handles the deletion of a poll .
     *
     * @param pollId the poll id
     * @param email  the email of the user
     */
private void deleteDoc(String pollId, String email) {
        Log.d(TAG, "deleteDoc: called for " + email);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                .collection("Polls").document(pollId).collection("Votes")
                .document(email).delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method updates the counter of a certain poll option of a poll
     * in case of selecting the option.
     *
     * @param pollId   the poll id
     * @param optionId the option id
     * @param i        the value of the counter
     */
private void updateCounter(final String pollId, final String optionId, final int i) {
        Log.d(TAG, "updateCounter:  called for "+optionId);

        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                .collection("Polls").document(pollId).collection("Options").document(optionId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String cnt=document.getData().get("count").toString();
                        int c= Integer.parseInt(cnt);
                        c+=i;

                        changeCounter(pollId,optionId,c);
                    } else {
                        pd.dismiss();


                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    pd.dismiss();
                }
            }
        });


    }

    /**
     *This method changes the value of the counter and updates to the database
     *
     * @param pollId   the poll id
     * @param optionId the option id
     * @param c        the count of the vote
     */
private void changeCounter(String pollId, String optionId, final int c) {
        Map<String , Object> map= new HashMap<>();
        map.put("count",c);
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                .collection("Polls").document(pollId).collection("Options").document(optionId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: changed "+String.valueOf(c));
                pd.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
        });
    }

    @NonNull
    @Override
    public PollOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.polloption_item_row,parent,false);
        context=parent.getContext();
        pd= new ProgressDialog(context);
        pd.setTitle("Please wait");
        return new PollOptionViewHolder(view);
    }

    /**
     * The holder class of poll option adapter which extends RecyclerView.ViewHolder.
     */
    class PollOptionViewHolder extends RecyclerView.ViewHolder{
        /**
         * The textView for Option name.
         */
        TextView optionName;
        /**
         * The TextView for vote count.
         */
        TextView txtVoteCount;
        /**
         * The ImageView for Check box.
         */
        public ImageView checkBox;


        /**
         * Instantiates a new Poll option view holder.
         *
         * @param itemView the item view
         */
        public PollOptionViewHolder(@NonNull View itemView) {
            super(itemView);
           optionName= itemView.findViewById(R.id.txtOptionName);
           txtVoteCount=itemView.findViewById(R.id.txtVoteCount);
           checkBox=itemView.findViewById(R.id.chkBox);


        }
    }



}
