package com.example.javaproject21;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The type Students list search activity.
 */
public class StudentsListSearchActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "StudentsListSearchActiv";
    /**
     * The EditText for search field.
     */
private EditText mSearchField;
    /**
     * The ImageButton for search btn.
     */
private ImageButton mSearchBtn;
    /**
     * The Recycler view variable.
     */
private RecyclerView recyclerView;
    /**
     * The ImageButton for back.
     */
    ImageButton btnBack;
    /**
     * The CollectionReference variable.
     */
    private CollectionReference ref= FirebaseFirestore.getInstance().collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list_search);
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);
        recyclerView=findViewById(R.id.result_list);
        btnBack=findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupRecView();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText=mSearchField.getText().toString();
                if(searchText.length()<1) setupRecView();
                else
                firebaseUserSearch(searchText);


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText=mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

    }

    /**
     * This method searches for the name of the student from the database which
     * matches with the given text mostly and a method is called for showing the
     * student info.
     *
     * @param searchText the search text
     */
private void firebaseUserSearch(String searchText) {
        Log.d(TAG, "firebaseUserSearch: started search for "+ searchText);
        Query query= FirebaseFirestore.getInstance().collection("Users").orderBy("name")
                .whereEqualTo("currentClass",Utils.getClassName()).startAt(searchText).endAt(searchText + "\uf8ff");
        FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class)
                .build();

        FirestoreRecyclerAdapter<Student,UsersViewHolder> studentSearchAdapter= new FirestoreRecyclerAdapter<Student, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull Student model) {
                holder.setDetails(getApplicationContext(),model.getName(),model.getBio(),model.getImageUri());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                        showStudentInfo(snapshot);

                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_item,parent,false);
                return new UsersViewHolder(view);
            }
        };
        recyclerView.setAdapter(studentSearchAdapter);
        studentSearchAdapter.startListening();

    }

    /**
     * This method sets up the recycler view by binding the holder to the
     * adapter and a method is called to show student info on click of a
     * item.
     */
private void setupRecView() {
        Log.d(TAG, "firebaseUserSearch: started setup ");
        Query query= FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass",Utils.getClassName());
        FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class)
                .build();

        FirestoreRecyclerAdapter<Student,UsersViewHolder> studentSearchAdapter= new FirestoreRecyclerAdapter<Student, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull Student model) {
                holder.setDetails(getApplicationContext(),model.getName(),model.getBio(),model.getImageUri());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                        showStudentInfo(snapshot);

                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_item,parent,false);
                return new UsersViewHolder(view);
            }
        };
        recyclerView.setAdapter(studentSearchAdapter);
        studentSearchAdapter.startListening();

    }

    /**
     * The holder class of the adapter which extends RecyclerView.ViewHolder .
     */
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
        public void setDetails(Context ctx, String userName, String userStatus, String userImage){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).placeholder(R.drawable.prof).into(user_image);


        }




    }

    /**
     * This method shows the info of the student on click of that item
     * of the recycler view by creating an alert dialog .
     *
     * @param snapshot the DocumentSnapshot of the student
     */
        private void showStudentInfo(DocumentSnapshot snapshot) {
        Student student=snapshot.toObject(Student.class);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.show_student_info_dialog, null);

        TextView txtName,txtEmail,txtBlood,txtPhoneNo,txtNickName,txtHobbies,txtAddress,txtBDay,txtHomeTown;
        CircleImageView profilePic;
        txtBlood=view.findViewById(R.id.txtBloodGroup);
        txtEmail=view.findViewById(R.id.txtEmail);
        txtName=view.findViewById(R.id.txtName);
        txtPhoneNo=view.findViewById(R.id.txtPhoneNo);
        profilePic=view.findViewById(R.id.profilePic);
        txtAddress=view.findViewById(R.id.txtAddress);
        txtBDay=view.findViewById(R.id.txtBirthday);
        txtHobbies=view.findViewById(R.id.txtHobbies);
        txtNickName=view.findViewById(R.id.txtNickname);
        txtHomeTown=view.findViewById(R.id.txtHometown);


        if(student!=null) {
            if (student.getBloodGroup() != null) txtBlood.setText(student.getBloodGroup());
            if (student.getEmail() != null) txtEmail.setText(student.getEmail());
            if (student.getName() != null) txtName.setText(student.getName());
            if (student.getPhoneNo() != null) txtPhoneNo.setText(student.getPhoneNo());
            if(student.getAddress()!=null) txtAddress.setText(student.getAddress());
            if(student.getDistrict()!=null) txtHomeTown.setText(student.getDistrict());
            if(student.getDateOfBirth()!=null) txtBDay.setText(student.getDateOfBirth());
            if(student.getNickName()!=null) txtNickName.setText(student.getNickName());
            if(student.getHobbies()!=null) txtHobbies.setText(student.getHobbies());

            Glide.with(view).load(student.getImageUri()).placeholder(R.drawable.prof).into(profilePic);
        }


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

