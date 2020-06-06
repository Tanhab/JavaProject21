package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The class for controlling class .
 */
public class ClassControlActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private  static final String TAG = "ClassControlActivity";
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;
    /**
     * The object of StudentAdapter.
     */
    StudentAdapter adapter;
    /**
     * The ImageButton for going back.
     */
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_control);
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
     * This method attaches the adapter to the recycler view and
     * calls another method for kicking out an user on click of an item.
     */
private void initRecView() {
        Query query= FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass",Utils.getClassName())
                ;
        FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class)
                .build();
        adapter= new StudentAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new StudentAdapter.StudentListener() {
            @Override
            public void handleStudent(DocumentSnapshot snapshot) {
                kickOutUserDialog(snapshot);
            }
        });
    }

    /**
     * This method creates an alert dialog for confirming whether the user
     * needs to be kicked out or not.
     *
     * @param snapshot the document snapshot of the user to be kicked out
     */
private void kickOutUserDialog(final DocumentSnapshot snapshot) {
        Student student=snapshot.toObject(Student.class);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClassControlActivity.this);
        alertDialogBuilder.setTitle("Kick out this user").setMessage("Are you sure ?\nOnce deleted it cannot be undone.")
                .setPositiveButton("Kick out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                kickOutUSer(snapshot);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        }).show();

    }

    /**
     * This method kicks out the user from the current class.
     *
     * @param snapshot the document snapshot of the user to be kicked out
     */
//private
    void kickOutUSer(DocumentSnapshot snapshot) {
        Student student=snapshot.toObject(Student.class);
        Map<String,Object> map= new HashMap<>();
        map.put("currentClass","empty");
        assert student != null;
        FirebaseFirestore.getInstance().collection("Users").document(student.getEmail()).update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ClassControlActivity.this, "User kicked out", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               e.printStackTrace();
                Toast.makeText(ClassControlActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
