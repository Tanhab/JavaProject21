package com.example.javaproject21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The adapter class for comment.
 */
public class CommentRecViewAdapter extends RecyclerView.Adapter<CommentRecViewAdapter.CommentViewHolder> {

    /**
     * The constant variable for logcat.
     */
private static final String TAG = "CommentRecViewAdapter";
    /**
     * The list variable for comments.
     */
private List<Comment> commentList;
    /**
     * The Context variable.
     */
private Context context;

    /**
     * Instantiates a new Comment rec view adapter.
     *
     * @param commentList the comment list
     */
    public CommentRecViewAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_row,parent,false);
        context=parent.getContext();
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment= commentList.get(position);
        holder.message.setText(comment.getMessage());
        holder.name.setText(comment.getSender());
        holder.date.setText(comment.getDate());
        Glide.with(holder.itemView).load(comment.getImageUri()).placeholder(R.drawable.prof).into(holder.userImage);



    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * The Holder class for comment.
     */
    class CommentViewHolder extends RecyclerView.ViewHolder{
        /**
         * The CircularImageView variable for userImage.
         */
        CircleImageView userImage;
        /**
         * The TextView variable for Name.
         */
        TextView name;
        /**
         *  The TextView variable for Date.
         */
        TextView date;
        /**
         *  The TextView variable for Message.
         */
        TextView message;

        /**
         * Instantiates a new Comment view holder.
         *
         * @param itemView the item view
         */
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.commentProfilePic);
            name=itemView.findViewById(R.id.txtNameComment);
            date=itemView.findViewById(R.id.txtDate);
            message=itemView.findViewById(R.id.commentMessage);

        }
    }
}
