package com.example.javaproject21;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PollRecyclerAdapter extends FirestoreRecyclerAdapter<Poll, PollRecyclerAdapter.PollViewHolder> {
    private static final String TAG = "PollRecyclerAdapter";
    private DocumentReference firebaseFirestore= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName());
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private RecyclerView.RecycledViewPool viewPool2=new RecyclerView.RecycledViewPool();
    private PollOptionRecAdapter pollOptionRecAdapter;

    private Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PollRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Poll> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PollViewHolder holder, int position, @NonNull Poll model) {
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(model.getSender().equals(email)||Utils.getCR2().equals(email)||Utils.getCR().equals(email)) {
            holder.btnDelete.setEnabled(true);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        holder.descView.setText(model.getMessage());
        holder.date.setText(model.getDate());
        holder.userName.setText(model.getSender());//change kora lagbe
        final String postId=model.getPollId();

        Glide.with(context).load(model.getImageUri()).placeholder(R.drawable.profile_placeholder).into(holder.userImage);


        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(postId).collection("Reacts").whereEqualTo("react","love").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();

                    holder.loveCount.setText(String.valueOf(count));

                } else {

                    holder.loveCount.setText("0");

                }

            }
        });
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(postId).collection("Reacts").whereEqualTo("react","angry").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();

                    holder.angryCount.setText(String.valueOf(count));

                } else {

                    holder.angryCount.setText("0");

                }

            }
        });
        final String currentUserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        if(currentUserId!=null) {
            firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "onEvent: " + documentSnapshot.toString());
                        String react = documentSnapshot.get("react").toString();
                        if (react.equals("love")) {
                            holder.iconLove.setImageDrawable(context.getDrawable(R.drawable.love_selected));
                            holder.iconAngry.setImageDrawable(context.getDrawable(R.drawable.angry_not_selected));
                        } else {
                            holder.iconLove.setImageDrawable(context.getDrawable(R.drawable.love_not_selected));
                            holder.iconAngry.setImageDrawable(context.getDrawable(R.drawable.angry_selected));

                        }

                    } else {
                        holder.iconAngry.setImageDrawable(context.getDrawable(R.drawable.angry_not_selected));
                        holder.iconLove.setImageDrawable(context.getDrawable(R.drawable.love_not_selected));


                    }

                }
            });
        }

        holder.iconLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert currentUserId != null;
                firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> likesMap = new HashMap<>();

                            likesMap.put("react","love");

                            firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).delete();

                        }

                    }
                });
            }
        });
        holder.iconAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert currentUserId != null;
                firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> likesMap = new HashMap<>();
                            // likesMap.put("timestamp", FieldValue.serverTimestamp());
                            likesMap.put("react","angry");

                            firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Polls/" + postId + "/Reacts").document(currentUserId).delete();

                        }

                    }
                });
            }
        });


        //comment er kaj
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.commentRecView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        Query nextQuery = FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(postId).collection("Comments")
                .orderBy("priority", Query.Direction.ASCENDING);
        Query defaultQuery = FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(postId).collection("Comments")
                .orderBy("priority", Query.Direction.ASCENDING).limit(2);
        /*FirestoreRecyclerOptions<Comment> options= new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(nextQuery,Comment.class)
                .build();*/
        final List<Comment> mainList=new ArrayList<>();
        layoutManager.setInitialPrefetchItemCount(mainList.size());

        final CommentRecViewAdapter commentRecViewAdapter= new CommentRecViewAdapter(mainList);
        //CommentRecyclerAdapter commentRecyclerAdapter=new CommentRecyclerAdapter(options);

        holder.commentRecView.setLayoutManager(layoutManager);
        holder.commentRecView.setAdapter(commentRecViewAdapter);
        holder.commentRecView.setRecycledViewPool(viewPool);
        //commentRecyclerAdapter.startListening();        //add listener
        final List<Comment> fullList= new ArrayList<>();



        nextQuery.addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Log.d(TAG, "onEvent: comment added : " + doc.toString());

                            Comment comment = doc.getDocument().toObject(Comment.class);
                            Log.d(TAG, "onEvent: comment= "+comment.toString());
                            if(mainList.size()<2)
                            {
                                mainList.add(comment);
                                fullList.add(comment);
                                commentRecViewAdapter.notifyDataSetChanged();
                            }else
                            {
                                fullList.add(comment);
                            }
                            /*model.getCommentList().add(comment);*/
                            //commentRecViewAdapter.notifyDataSetChanged();
                            int sz=fullList.size();
                            if(sz<2) holder.commentCount.setText(String.valueOf(sz)+ " comment");
                            else {

                                holder.commentCount.setText(String.valueOf(sz) + " comments");

                            }
                            if(sz>2)
                            {
                                holder.txtShowAllDocument.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }

            }
        });
        holder.txtShowAllDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=holder.txtShowAllDocument.getText().toString();
                if(s.equals("View all comments"))
                {
                    mainList.clear();
                    mainList.addAll(fullList);
                    commentRecViewAdapter.notifyDataSetChanged();
                    holder.txtShowAllDocument.setText("Show less comments");

                }else{
                    mainList.clear();
                    if(fullList.size()>0) mainList.add(fullList.get(0));
                    if(fullList.size()>1)mainList.add(fullList.get(1));
                    commentRecViewAdapter.notifyDataSetChanged();
                    holder.txtShowAllDocument.setText("View all comments");
                }
            }
        });



        //add comment
        holder.iconSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=holder.txtComment.getText().toString();

                Calendar calendar = Calendar.getInstance();
                //String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                int HOUR = calendar.get(Calendar.HOUR);
                int MINUTE = calendar.get(Calendar.MINUTE);
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);
                int SECOND= calendar.get(Calendar.SECOND);
                long a=YEAR*100+MONTH;
                a=a*100+DATE;
                a=a*100+HOUR;
                a=a*100+MINUTE;
                a=a*100+SECOND;
                String time = DateFormat.format("h:mm a", calendar).toString();
                String date =DateFormat.format("dd.MM.yy", calendar).toString();

                date= time + " " + date;
                if(msg.length()>0){
                    Comment comment= new Comment(Utils.getUserName(),msg,date,Utils.getImageUri(),a);
                    FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(postId).collection("Comments").add(comment)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: "+documentReference.toString());
                                    holder.txtComment.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        //option setup;
        LinearLayoutManager optionLayout = new LinearLayoutManager(
                holder.optionRecView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        CollectionReference ref= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Polls").document(postId).collection("Options");
        Query optionQuery= ref.orderBy("count", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PollOption> options= new FirestoreRecyclerOptions.Builder<PollOption>()
                .setQuery(optionQuery,PollOption.class)
                .build();
        pollOptionRecAdapter= new PollOptionRecAdapter(options);
        holder.optionRecView.setRecycledViewPool(viewPool2);
        holder.optionRecView.setLayoutManager(optionLayout);
        holder.optionRecView.setAdapter(pollOptionRecAdapter);

        pollOptionRecAdapter.startListening();;


    }
    private PollListener pollListener;
    public void setOnItemClickListener(PollListener listener) {
        this.pollListener = listener;

    }

    public interface PollListener {
        public void handleStudent(DocumentSnapshot snapshot);

    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_item_row, parent, false);
        context = parent.getContext();

        return new PollViewHolder(view);
    }

    class PollViewHolder extends RecyclerView.ViewHolder{
        private View mView;

        private TextView descView,txtShowAllDocument;;
        private TextView date;

        private TextView userName;
        private CircleImageView userImage;

        private ImageView iconLove;
        private TextView loveCount;
        private ImageView iconAngry;
        private TextView angryCount;
        private EditText txtComment;
        private ImageView iconComment;
        private TextView commentCount;
        private RecyclerView commentRecView,optionRecView;
        private ImageView iconSend;
        ImageButton btnDelete;




        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnDelete.setEnabled(false);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    pollListener.handleStudent(snapshot);

                }
            });

            descView=itemView.findViewById(R.id.txtDescription);
            date=itemView.findViewById(R.id.txtDate);
            userName=itemView.findViewById(R.id.txtName);
            userImage=itemView.findViewById(R.id.postProfilePic);
            iconLove=itemView.findViewById(R.id.iconLove);
            loveCount=itemView.findViewById(R.id.txtLoveCount);
            iconAngry=itemView.findViewById(R.id.iconAngry);
            commentCount=itemView.findViewById(R.id.txtCommentCount);
            angryCount=itemView.findViewById(R.id.txtAngryCount);
            commentRecView=itemView.findViewById(R.id.commentRecycler);
            iconSend=itemView.findViewById(R.id.iconSend);
            txtComment=itemView.findViewById(R.id.txtComment);
            txtShowAllDocument=itemView.findViewById(R.id.txtShowAllComments);
            optionRecView=itemView.findViewById(R.id.optionRecyclerView);


        }
    }



}
