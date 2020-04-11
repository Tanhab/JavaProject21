package com.example.javaproject21;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private CardView cardClassRoutine,cardExamRoutine,cardNotification,cardResources,cardCR,cardProfile;
    private CircleImageView imageView;
    private TextView txtStudentName;
    ImageButton btnNavigation;

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
        btnNavigation=view.findViewById(R.id.btnNavigation);

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
                if(isCR())
                startActivity(new Intent(view.getContext(),CRActivity.class));
                else{
                    Toast.makeText(view.getContext(), "Sorry, but you are not a CR.", Toast.LENGTH_LONG).show();
                }

            }
        });
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),MyClassroomActivity.class));

            }
        });
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout navDrawer = Objects.requireNonNull(getActivity()).findViewById(R.id.drawer);
                // If the navigation drawer is not open then open it, if its already open then close it.
                if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
                else navDrawer.closeDrawer(GravityCompat.END);
            }
        });
        return  view;
    }

    private boolean isCR() {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(Utils.getCR().equals(email)|| Utils.getCR2().equals(email))
            return  true;
        else return  false;
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
                        Uri uri=Uri.parse(imageUrl);
                        StorageReference ref= FirebaseStorage.getInstance().getReference().child(imageUrl);
                        Glide.with(view).setDefaultRequestOptions(new RequestOptions().timeout(30*1000)).load(uri).placeholder(R.drawable.classroom).into(imageView);
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
