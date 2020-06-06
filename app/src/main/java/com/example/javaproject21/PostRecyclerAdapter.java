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

public class PostRecyclerAdapter extends FirestoreRecyclerAdapter<Post, PostRecyclerAdapter.PostViewHolder> {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "PostRecyclerAdapter";
    /**
     * The DocumentReference variable.
     */
    DocumentReference firebaseFirestore= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName());
    /**
     * The Context variable.
     */
    Context context;
    /**
     * The object of RecyclerView.RecycledViewPool.
     */
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull Post model) {
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(model.getSender().equals(email)||Utils.getCR2().equals(email)||Utils.getCR().equals(email)) {
            holder.btnDelete.setEnabled(true);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        holder.descView.setText(model.getMessage());
        holder.date.setText(model.getDate());
        holder.userName.setText(model.getSender());//change kora lagbe
        final String postId=model.getPostID();

        Glide.with(context).load(model.getImageUri()).placeholder(R.drawable.profile_placeholder).into(holder.userImage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.commentRecView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Posts").document(postId).collection("Reacts").whereEqualTo("react","love").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots != null;
                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();

                    holder.loveCount.setText(String.valueOf(count));

                } else {

                    holder.loveCount.setText("0");

                }

            }
        });
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Posts").document(postId).collection("Reacts").whereEqualTo("react","angry").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots != null;
                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();

                    holder.angryCount.setText(String.valueOf(count));

                } else {

                    holder.angryCount.setText("0");

                }

            }
        });
        final String currentUserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        assert currentUserId != null;
        firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){
                    Log.d(TAG, "onEvent: "+documentSnapshot.toString());
                    String react= documentSnapshot.get("react").toString();
                    if(react.equals("love"))
                    {
                        holder.iconLove.setImageDrawable(context.getDrawable(R.drawable.love_selected));
                        holder.iconAngry.setImageDrawable(context.getDrawable(R.drawable.angry_not_selected));
                    }else{
                        holder.iconLove.setImageDrawable(context.getDrawable(R.drawable.love_not_selected));
                        holder.iconAngry.setImageDrawable(context.getDrawable(R.drawable.angry_selected));

                    }

                }else{
                    holder.iconAngry.setImageDrawable(context.getDrawable(R.drawable.angry_not_selected));
                    holder.iconLove.setImageDrawable(context.getDrawable(R.drawable.love_not_selected));


                }

            }
        });

        holder.iconLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> likesMap = new HashMap<>();

                            likesMap.put("react","love");

                            firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).delete();

                        }

                    }
                });
            }
        });
        holder.iconAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> likesMap = new HashMap<>();
                            // likesMap.put("timestamp", FieldValue.serverTimestamp());
                            likesMap.put("react","angry");

                            firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Posts/" + postId + "/Reacts").document(currentUserId).delete();

                        }

                    }
                });
            }
        });


        //comment er kaj
        final List<Comment> mainList=new ArrayList<>();
        layoutManager.setInitialPrefetchItemCount(mainList.size());
        Query nextQuery = FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Posts").document(postId).collection("Comments")
                .orderBy("priority", Query.Direction.ASCENDING);
        Query defaultQuery = FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Posts").document(postId).collection("Comments")
                .orderBy("priority", Query.Direction.ASCENDING).limit(2);
        /*FirestoreRecyclerOptions<Comment> options= new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(nextQuery,Comment.class)
                .build();*/

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
                            else holder.commentCount.setText(String.valueOf(sz)+ " comments");
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

                date= time + " " + date;
                //String uri="https://scontent.fdac12-1.fna.fbcdn.net/v/t1.15752-9/89257742_197521211473213_7312614522215202816_n.jpg?_nc_cat=110&_nc_sid=b96e70&_nc_eui2=AeEqaKjSMyB3uO1pNIPRAoub_mgegDv5LfT-aB6AO_kt9NgqUyz9r5rFAXW8nHUJdS-UcQuI9TuFhlcIqlEhbmWF&_nc_ohc=pCD-e6kdnEsAX8FV1bL&_nc_ht=scontent.fdac12-1.fna&oh=415c98ca2cbb7b0aed0d936efff03232&oe=5EC040CA";
                if(msg.length()>0){
                    Comment comment= new Comment(Utils.getUserName(),msg,date,Utils.getImageUri(),a);
                    FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Posts").document(postId).collection("Comments").add(comment)
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
    }
    /**
     * The object of The Post listener.
     */
    private PostListener postListener;
    /**
     * Sets on item click listener.
     *
     * @param listener the object of PostListener
     */
    public void setOnItemClickListener(PostListener listener) {
        this.postListener = listener;

    }
    /**
     * The interface Post listener.
     */

    public interface PostListener {
        /**
         * The abstract method of the interface.
         *
         * @param snapshot the snapshot
         */
        public void handleStudent(DocumentSnapshot snapshot);

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_single_layout, parent, false);
        context = parent.getContext();

        return new PostViewHolder(view);
    }
    /**
     * The holder class of PostRecyclerAdapter.
     */
    class PostViewHolder extends RecyclerView.ViewHolder{
        /**
         * The view variable.
         */
        private View mView;

        /**
         * The TextView variable for description.
         */
        private TextView descView;
        /**
         * The TextView variable for show all documents.
         */
        private TextView txtShowAllDocument;
        /**
         * The TextView variable for Date.
         */
        private TextView date;
        /**
         *  TextView variable for User name.
         */
        private TextView userName;
        /**
         * The CircleImageView variable for User image.
         */
        private CircleImageView userImage;

        /**
         * The ImageView variable for love icon.
         */
        private ImageView iconLove;
        /**
         * The TextView variable for Love count.
         */
        private TextView loveCount;
        /**
         * The ImageView variable for angry icon.
         */
        private ImageView iconAngry;
        /**
         * The TextView variable for Angry count.
         */
        private TextView angryCount;
        /**
         * The EditText variable for comment.
         */
        private EditText txtComment;
        /**
         * The ImageView variable for comment.
         */
        private ImageView iconComment;
        /**
         * The TextView variable for Comment count.
         */
        private TextView commentCount;
        /**
         * The RecyclerView variable for Comment .
         */
        private RecyclerView commentRecView;
        /**
         * The ImageView variable for send.
         */
        private ImageView iconSend;
        /**
         * The ImageButton variable for delete.
         */
        ImageButton btnDelete;



        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnDelete.setEnabled(false);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    postListener.handleStudent(snapshot);

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


        }
    }

}
