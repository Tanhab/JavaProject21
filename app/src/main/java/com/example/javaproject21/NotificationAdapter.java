package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotificationAdapter extends FirestoreRecyclerAdapter<Notification, NotificationAdapter.NotificationViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<Notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
                    holder.txtMsg.setText(model.getBody());
                    holder.txtTitle.setText(model.getTitle());
                    holder.txtSender.setText(model.getSender());
                    holder.txtDate.setText(model.getDate());
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row,parent,false);
        return new NotificationViewHolder(view);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle,txtMsg,txtDate,txtSender;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.txtNotificationTitle);
            txtMsg= itemView.findViewById(R.id.notificationMessage);
            txtSender= itemView.findViewById(R.id.notificationSender);
            txtDate= itemView.findViewById(R.id.notificationDate);

        }
    }
}
