package com.example.javaproject21;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private CardView cardClassRoutine,cardExamRoutine,cardNotification,cardResources,cardCR,cardProfile;
    private CircleImageView imageView;
    private TextView txtStudentName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         final View view=inflater.inflate(R.layout.fragment_main, container, false);

        cardClassRoutine=view.findViewById(R.id.cardClassRoutine);
        cardCR=view.findViewById(R.id.cardCR);
        cardExamRoutine=view.findViewById(R.id.cardExamRoutine);
        cardNotification=view.findViewById(R.id.cardNotification);
        cardResources=view.findViewById(R.id.cardResources);
        cardProfile=view.findViewById(R.id.cardProfile);
        imageView=view.findViewById(R.id.imageProfile);
        txtStudentName=view.findViewById(R.id.studentName);

        setupTopView(view);

        cardClassRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),ClassRoutineActivity.class));

            }
        });
        cardExamRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),ExamRoutineActivity.class));

            }
        });
        cardResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),ResourcesActivity.class));
            }
        });
        cardNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),NotificationActivity.class));

            }
        });
        cardCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),CRActivity.class));

            }
        });
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),MyClassroomActivity.class));

            }
        });


        return  view;
    }

    private void setupTopView(final View view) {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore.getInstance().collection("Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name= documentSnapshot.getData().get("name").toString();
                        String imageUrl=documentSnapshot.getData().get("imageUri").toString();
                        txtStudentName.setText(name);
                        Glide.with(view).load(imageUrl).placeholder(R.drawable.classroom).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setupTopView(getView());
    }
}
