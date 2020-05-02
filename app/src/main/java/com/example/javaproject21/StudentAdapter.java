package com.example.javaproject21;

import android.content.Context;
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
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends FirestoreRecyclerAdapter<Student, StudentAdapter.StudentViewHolder> {

private Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StudentAdapter(@NonNull FirestoreRecyclerOptions<Student> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull Student model) {
        holder.setDetails(context,model.getName(),model.getBio(),model.getImageUri());


    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_item,parent,false);
       context=parent.getContext();
       return new StudentViewHolder(view);
    }
    public void deleteItem(DocumentSnapshot snapshot){
        snapshot.getReference().delete();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    studentListener.handleStudent(snapshot);

                }
            });

        }
        public void setDetails(Context ctx, String userName, String userStatus, String userImage){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).into(user_image);


        }




    }

    private StudentListener studentListener;
    public void setOnItemClickListener(StudentListener listener) {
        this.studentListener = listener;

    }

    public interface StudentListener {
        public void handleStudent(DocumentSnapshot snapshot);

    }
}
