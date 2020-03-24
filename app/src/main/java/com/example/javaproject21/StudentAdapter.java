package com.example.javaproject21;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends FirestoreRecyclerAdapter<Student, StudentAdapter.StudentViewHolder> {
StudentListener studentListener;
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
        Glide.with(holder.itemView).load(model.imageUri).placeholder(R.drawable.ic_profile_empty).into(holder.profilePic);
        holder.txtName.setText(model.getName());
        holder.txtEmail.setText(model.getEmail());
        holder.txtBloodGroup.setText(model.getBloodGroup());


    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row,parent,false);
       return new StudentViewHolder(view);
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView txtName,txtEmail,txtBloodGroup;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.imageProfile);
            txtBloodGroup=itemView.findViewById(R.id.bloodGroup);
            txtEmail=itemView.findViewById(R.id.studentEmail);
            txtName=itemView.findViewById(R.id.studentName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    studentListener.handleStudent(snapshot);

                }
            });
        }
        public void deleteItem() {
            studentListener.handleDeleteItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }

    }
    public void setOnItemClickListener(StudentListener listener) {
        this.studentListener = listener;

    }

    public interface StudentListener {
        public void handleStudent(DocumentSnapshot snapshot);
        public void handleDeleteItem(DocumentSnapshot snapshot);
    }
}
