package com.example.javaproject21;

import android.app.Activity;
import android.app.AlertDialog;
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

/**
 * The type Event adapter.
 */
public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventviewHolder> {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "EventAdapter";
    /**
     * The DocumentReference variable.
     */
    DocumentReference firebaseFirestore= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName());
    /**
     * The Context variable.
     */
    Context context;
    /**
     * The object of RecycledViewPool of RecyclerView.
     */
private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
private RecyclerView recyclerView;
private VoterRecAdapter voterRecAdapter;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions}* for configuration options.
     *
     * @param options the options
     */
    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final EventviewHolder holder, int position, @NonNull Event model) {
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(model.getSender().equals(email)||Utils.getCR2().equals(email)||Utils.getCR().equals(email)) {
            holder.btnDelete.setEnabled(true);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        holder.classes.setText(model.getMessage());
        holder.eventName.setText(model.getEventName());

        holder.eventDate.setText("Date: "+model.getEventDate());
        holder.eventTime.setText("Time: "+model.getTime());
        holder.eventPlace.setText("Place: "+model.getEventPlace());
        holder.date.setText(model.getDate());
        holder.userName.setText(model.getSender());//change kora lagbe
        final String postId=model.getPostID();

        Glide.with(context).load(model.getImageUri()).placeholder(R.drawable.prof).into(holder.userImage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.commentRecView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        Log.d(TAG, "onBindViewHolder: "+postId);

        firebaseFirestore.collection("Events").document(postId).collection("Reacts").whereEqualTo("react","love").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
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
        firebaseFirestore.collection("Events").document(postId).collection("Reacts").whereEqualTo("react","angry").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
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
        firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e!=null){
                    e.printStackTrace();
                    return;
                }

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

                firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!Objects.requireNonNull(task.getResult()).exists()){

                            Map<String, String> likesMap = new HashMap<>();

                            likesMap.put("react","love");

                            firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).delete();

                        }

                    }
                });
            }
        });
        holder.iconAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> likesMap = new HashMap<>();
                            // likesMap.put("timestamp", FieldValue.serverTimestamp());
                            likesMap.put("react","angry");

                            firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Events/" + postId + "/Reacts").document(currentUserId).delete();

                        }

                    }
                });
            }
        });
            //option
       /* final CollectionReference ref= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Events").document(postId).collection("Choices");
        ref.whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        });*/
        firebaseFirestore.collection("Events").document(postId).collection("Choices").whereEqualTo("choice","going").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots != null;
                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();

                    if(count>1) holder.txtGoingCount.setText(String.valueOf(count)+ " persons");
                    else holder.txtGoingCount.setText(String.valueOf(count)+ " person");
                } else {

                    holder.txtGoingCount.setText("0 person");

                }

            }
        });
        firebaseFirestore.collection("Events").document(postId).collection("Choices").whereEqualTo("choice","notGoing").addSnapshotListener((Activity) context,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();


                    if(count>1) holder.txtNotGoingCount.setText(String.valueOf(count)+ " persons");
                    else holder.txtNotGoingCount.setText(String.valueOf(count)+ " person");


                } else {

                    holder.txtNotGoingCount.setText("0 person");

                }

            }
        });
       // final String currentUserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).addSnapshotListener((Activity) context,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){
                    Log.d(TAG, "onEvent: "+documentSnapshot.toString());
                    String choice= documentSnapshot.get("choice").toString();
                    if(choice.equals("going"))
                    {
                        holder.goingBox.setImageDrawable(context.getDrawable(R.drawable.checked));
                        holder.notGoingBox.setImageDrawable(context.getDrawable(R.drawable.unchecked));
                    }else{
                        holder.goingBox.setImageDrawable(context.getDrawable(R.drawable.unchecked));
                        holder.notGoingBox.setImageDrawable(context.getDrawable(R.drawable.checked));

                    }

                }else{
                    holder.goingBox.setImageDrawable(context.getDrawable(R.drawable.unchecked));
                    holder.notGoingBox.setImageDrawable(context.getDrawable(R.drawable.unchecked));
                }

            }
        });

        holder.txtGoingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.poll_checkedlist_dialog, null);
                ImageButton cancelButton = view.findViewById(R.id.closeBtnId);
                RecyclerView recyclerView =view.findViewById(R.id.stdlistRecId);
                recyclerView.setLayoutManager(new LinearLayoutManager( context));


                Query query= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                        .collection("Events").document(postId).collection("Choices")
                        .whereEqualTo("choice","going");
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
        });

        holder.txtNotGoingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.poll_checkedlist_dialog, null);
                ImageButton cancelButton = view.findViewById(R.id.closeBtnId);
                RecyclerView recyclerView =view.findViewById(R.id.stdlistRecId);
                recyclerView.setLayoutManager(new LinearLayoutManager( context));


                Query query= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName())
                        .collection("Events").document(postId).collection("Choices")
                        .whereEqualTo("choice","notGoing");
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
        });

        holder.goingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> choiceMap = new HashMap<>();

                            choiceMap.put("choice","going");
                            choiceMap.put("postId",postId);
                            choiceMap.put("name",Utils.getUserName());
                            choiceMap.put("imageUri",Utils.getImageUri());


                            firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).set(choiceMap).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        } else {

                            firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).delete();

                        }

                    }
                });
            }
        });
        holder.notGoingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, String> choiceMap = new HashMap<>();
                            choiceMap.put("postId",postId);
                            choiceMap.put("choice","notGoing");
                            choiceMap.put("name",Utils.getUserName());
                            choiceMap.put("imageUri",Utils.getImageUri());

                            firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).set(choiceMap);

                        } else {

                            firebaseFirestore.collection("Events/" + postId + "/Choices").document(currentUserId).delete();

                        }

                    }
                });
            }
        });




        //done
        final List<Comment> mainList=new ArrayList<>();
        //comment er kaj
        layoutManager.setInitialPrefetchItemCount(mainList.size());
        Query nextQuery = firebaseFirestore.collection("Events").document(postId).collection("Comments")
                .orderBy("priority", Query.Direction.ASCENDING);
        Query defaultQuery = firebaseFirestore.collection("Events").document(postId).collection("Comments")
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
                if(msg.length()>0){
                    Comment comment= new Comment(Utils.getUserName(),msg,date,Utils.getImageUri(),a);
                    firebaseFirestore.collection("Events").document(postId).collection("Comments").add(comment)
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
     * The object of EventsListener.
     */
private EventsListener studentListener;

    /**
     * Sets on item click listener.
     *
     * @param listener the object of EventsListener
     */
    public void setOnItemClickListener(EventsListener listener) {
        this.studentListener = listener;

    }

    /**
     * The interface EventsListener with abstract method.
     */
    public interface EventsListener {
        /**
         * The abstract method handleStudent.
         *
         * @param snapshot the documents of the student
         */
        public void handleStudent(DocumentSnapshot snapshot);

    }
    @NonNull
    @Override
    public EventviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_single_item,parent,false);
        context=parent.getContext();
        return  new EventviewHolder(view);
    }

    /**
     * The holder class which extends RecyclerView.ViewHolder.
     */
    class EventviewHolder extends RecyclerView.ViewHolder{
        /**
         * The View varible.
         */
private View mView;

        /**
         * The TextView for Desc view.
         */
private TextView descView;
        /**
         * The TextView for show all document.
         */
private TextView txtShowAllDocument;;
        /**
         * The TextView for Date.
         */
private TextView date;

        /**
         * The TextView for User name.
         */
private TextView userName;
        /**
         * The CircularImage variable image.
         */
private CircleImageView userImage;

        /**
         * The ImageView for love.
         */
private ImageView iconLove;
        /**
         * The TextView for Love count.
         */
private TextView loveCount;
        /**
         * The ImageView for Icon angry.
         */
private ImageView iconAngry;
        /**
         * The TextView for Angry count.
         */
private TextView angryCount;
        /**
         * The EditText for comment.
         */
private EditText txtComment;
        /**
         * The ImageView for Icon comment.
         */
private ImageView iconComment;
        /**
         * The TextView for Comment count.
         */
private TextView commentCount;
        /**
         * The RecyclerView variable.
         */
private RecyclerView commentRecView;
        /**
         * The ImageView for Icon send.
         */
private ImageView iconSend;
        /**
         * The TextView for Section.
         */
private TextView section;
        /**
         * The TextView for Classes.
         */
private TextView classes;
        /**
         * The TextView for Event name.
         */
private TextView eventName;
        /**
         * The TextView for Event date.
         */
private TextView eventDate;
        /**
         * The TextView for Event time.
         */
private TextView eventTime;
        /**
         * The TextView for going count.
         */
private TextView txtGoingCount;
        /**
         * The TextView for not going count.
         */
private TextView txtNotGoingCount;
        /**
         * The TextView for Event place.
         */
private
        TextView eventPlace;
        /**
         * The ImageView for Going box.
         */
        ImageView goingBox;
        /**
         * The ImageView for Not going box.
         */
        ImageView notGoingBox;
        /**
         * The ImageButton for delete.
         */
        ImageButton btnDelete;

        /**
         * Instantiates a new Eventview holder.
         *
         * @param itemView the item view
         */
        public EventviewHolder(@NonNull View itemView) {
            super(itemView);


            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnDelete.setEnabled(false);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    studentListener.handleStudent(snapshot);

                }
            });
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
            eventDate=itemView.findViewById(R.id.txtDateRoutine);
            eventName=itemView.findViewById(R.id.text1);
            eventTime=itemView.findViewById(R.id.txtTime);
            txtGoingCount=itemView.findViewById(R.id.txtGoingCount);
            txtNotGoingCount=itemView.findViewById(R.id.txtNotGoingCount);
            goingBox=itemView.findViewById(R.id.goingBox);
            notGoingBox=itemView.findViewById(R.id.notGoingBox);
            classes=itemView.findViewById(R.id.txtClasses);
            eventPlace=itemView.findViewById(R.id.txtEventPlace);

        }
    }
}
