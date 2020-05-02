package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentsListSearchActivity extends AppCompatActivity {
    private static final String TAG = "StudentsListSearchActiv";
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView recyclerView;
    ImageButton btnBack;
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

    }private void setupRecView() {
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
    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setDetails(Context ctx, String userName, String userStatus, String userImage){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).placeholder(R.drawable.prof).into(user_image);


        }




    }
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
