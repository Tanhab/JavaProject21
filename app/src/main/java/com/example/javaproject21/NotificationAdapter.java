package com.example.javaproject21;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The class Notification adapter.
 */
public class NotificationAdapter extends FirestoreRecyclerAdapter<Notification, NotificationAdapter.NotificationViewHolder> {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "NotificationAdapter";
    /**
     * The Context variable.
     */
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions}* for configuration options.
     *
     * @param options the options
     */
    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<Notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
                    holder.txtMsg.setText(model.getMessage());
        Glide.with(context).load(model.getImageUri()).placeholder(R.drawable.ic_profile_empty).into(holder.profilePic);

                    holder.txtSender.setText(model.getSender());
                    holder.txtDate.setText(model.getDate());
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row,parent,false);
        context=parent.getContext();
        return new NotificationViewHolder(view);
    }

    /**
     * The holder class of Notification which extends view holder of recycler view.
     */
    class NotificationViewHolder extends RecyclerView.ViewHolder{
        /**
         * The Textview for message.
         */
        TextView txtMsg;
        /**
         * The Textview for date.
         */
        TextView txtDate;
        /**
         * TThe Textview for sender.
         */
        TextView txtSender;
        /**
         * The  CircleImageView for Profile pic.
         */
        CircleImageView profilePic;

        /**
         * Instantiates a new Notification view holder.
         *
         * @param itemView the item view
         */
        public NotificationViewHolder(@NonNull final View itemView) {
            super(itemView);

            txtMsg= itemView.findViewById(R.id.txtDescription);
            txtSender= itemView.findViewById(R.id.txtName);
            txtDate= itemView.findViewById(R.id.txtDate);
            profilePic=itemView.findViewById(R.id.postProfilePic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION ){
                        Notification notification = getSnapshots().getSnapshot(position).toObject(Notification.class);
                        assert notification != null;
                        switch (notification.getActivity()){
                            case "PollFragment":
                                Intent intent= new Intent(context,TimelineActivity.class);
                                intent.putExtra("EXTRA","PollFragment");
                                context.startActivity(intent);
                                break;
                            case "ClassRoutine":
                                context.startActivity(new Intent(context,ClassRoutineActivity.class));
                                break;
                            case "ExamRoutine":
                                context.startActivity(new Intent(context,ExamRoutineActivity.class));
                                break;
                            case "TimelineFragment":
                                Intent intent2= new Intent(context,TimelineActivity.class);
                                intent2.putExtra("EXTRA","TimelineFragment");
                                context.startActivity(intent2);
                                break;
                            case "EventsFragment":
                                Intent intent3= new Intent(context,TimelineActivity.class);
                                intent3.putExtra("EXTRA","EventsFragment");
                                context.startActivity(intent3);
                                break;

                            default:
                                break;


                        }
                    }
                }
            });

        }
    }
}
