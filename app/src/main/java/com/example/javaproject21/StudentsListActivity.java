package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The class for Students list activity.
 */
public class StudentsListActivity extends AppCompatActivity  {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "StudentsListActivity";
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;
    /**
     * The object of StudentAdapter.
     */
    StudentAdapter adapter;
    /**
     * The ImageButton for back.
     */
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        recyclerView=findViewById(R.id.recyclerView);
        initRecView();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    /**
     * This method sets the adapter to the recycler view of the student
     * list and calls a method on clicking an item.
     */
private void initRecView() {
        Query query= FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass",Utils.getClassName())
                ;
        FirestoreRecyclerOptions<Student>options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class)
                .build();
        adapter= new StudentAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new StudentAdapter.StudentListener() {
            @Override
            public void handleStudent(DocumentSnapshot snapshot) {
                showStudentInfo(snapshot);
            }

        });


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

        TextView txtName,txtEmail,txtBlood,txtPhoneNo;
        CircleImageView profilePic;
        txtBlood=view.findViewById(R.id.txtBloodGroup);
        txtEmail=view.findViewById(R.id.txtEmail);
        txtName=view.findViewById(R.id.txtName);
        txtPhoneNo=view.findViewById(R.id.txtPhoneNo);
        profilePic=view.findViewById(R.id.profilePic);


       if(student!=null) {
           if (student.getBloodGroup() != null) txtBlood.setText(student.getBloodGroup());
           if (student.getEmail() != null) txtEmail.setText(student.getEmail());
           if (student.getName() != null) txtName.setText(student.getName());
           if (student.getPhoneNo() != null) txtPhoneNo.setText(student.getPhoneNo());
           Glide.with(view).load(student.getImageUri()).placeholder(R.drawable.ic_profile_empty).into(profilePic);
       }


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
