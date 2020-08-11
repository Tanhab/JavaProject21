package com.example.javaproject21;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class VoterRecAdapter extends FirestoreRecyclerAdapter<Student, VoterRecAdapter.VoterRecHolder> {

    private Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VoterRecAdapter(@NonNull FirestoreRecyclerOptions<Student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VoterRecHolder holder, int position, @NonNull Student model) {

        holder.setDetails(context,model.getName(),model.getImageUri());
    }

    @NonNull
    @Override
    public VoterRecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_checkedlist_row,parent,false);
        context=parent.getContext();
        return new VoterRecHolder(view);
    }

    public class VoterRecHolder extends RecyclerView.ViewHolder {

        private static final String TAG ="ViewHolder" ;
        /**
         * The view variable.
         */
        View mView;

        /**
         * Instantiates a new Student view holder.
         *
         * @param itemView the item view
         */
        public VoterRecHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

        }

        /**
         * This method sets the details of a user to the holder of the recycler adapter.
         *
         * @param ctx        the context
         * @param userName   the user name
         * @param userImage  the user image
         */
        public void setDetails(Context ctx, String userName,String userImage){

            TextView user_name =  mView.findViewById(R.id.txtName);
            ImageView user_image = mView.findViewById(R.id.postProfilePic);
            user_name.setText(userName);
            Log.d(TAG, "setDetails: "+ user_image);
            Glide.with(ctx).load(userImage).into(user_image);


        }
    }
}
