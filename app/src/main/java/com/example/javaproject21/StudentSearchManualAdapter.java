package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentSearchManualAdapter extends  RecyclerView.Adapter<StudentSearchManualAdapter.UsersViewHolder> {
    private static final String TAG = "StudentSearchManualAdap";
    private List<Student> studentList;
    private Context context;

    public StudentSearchManualAdapter(List<Student> s) {
        studentList = s;
    }

    public void update(List<Student>s)
    {
        studentList=s;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_item, parent, false);
        context = parent.getContext();
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersViewHolder holder, final int position) {
        final Student model = studentList.get(position);
        holder.setDetails(context, model.getName(), model.getBio(), model.getImageUri());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                showStudentInfo(model);


            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        /**
         * The view variable.
         */
        View mView;

        /**
         * Instantiates a new Users view holder.
         *
         * @param itemView the item view
         */
        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        /**
         * This method sets the details of a user to the holder of the recycler adapter.
         *
         * @param ctx        the context
         * @param userName   the user name
         * @param userStatus the user status
         * @param userImage  the user image
         */
        public void setDetails(Context ctx, String userName, String userStatus, String userImage) {

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).placeholder(R.drawable.prof).into(user_image);

        }


    }


    void showStudentInfo(Student student) {
        //Student student=snapshot.toObject(Student.class);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_student_info_dialog, null);

        TextView txtName, txtEmail, txtBlood, txtPhoneNo, txtNickName, txtHobbies, txtAddress, txtBDay, txtHomeTown;
        CircleImageView profilePic;
        txtBlood = view.findViewById(R.id.txtBloodGroup);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtName = view.findViewById(R.id.txtName);
        txtPhoneNo = view.findViewById(R.id.txtPhoneNo);
        profilePic = view.findViewById(R.id.profilePic);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtBDay = view.findViewById(R.id.txtBirthday);
        txtHobbies = view.findViewById(R.id.txtHobbies);
        txtNickName = view.findViewById(R.id.txtNickname);
        txtHomeTown = view.findViewById(R.id.txtHometown);


        if (student != null) {
            if (student.getBloodGroup() != null) txtBlood.setText(student.getBloodGroup());
            if (student.getEmail() != null) txtEmail.setText(student.getEmail());
            if (student.getName() != null) txtName.setText(student.getName());
            if (student.getPhoneNo() != null) txtPhoneNo.setText(student.getPhoneNo());
            if (student.getAddress() != null) txtAddress.setText(student.getAddress());
            if (student.getDistrict() != null) txtHomeTown.setText(student.getDistrict());
            if (student.getDateOfBirth() != null) txtBDay.setText(student.getDateOfBirth());
            if (student.getNickName() != null) txtNickName.setText(student.getNickName());
            if (student.getHobbies() != null) txtHobbies.setText(student.getHobbies());

            Glide.with(view).load(student.getImageUri()).placeholder(R.drawable.prof).into(profilePic);
        }


        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        alertDialog.show();

    }
}



