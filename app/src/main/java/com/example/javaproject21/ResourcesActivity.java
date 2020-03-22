package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class ResourcesActivity extends AppCompatActivity {
    private static final String TAG = "ResourcesActivity";
    RecyclerView recyclerView;
    private FloatingActionButton fab;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference ref= db.collection("cse18").document("Data").collection("Folders");
    private FolderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        recyclerView=findViewById(R.id.folderRecView);
        fab=findViewById(R.id.fab);
        setupRecView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFolder();
            }
        });

    }

    private void addFolder() {


            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.add_folder_dialog, null);

            Button acceptButton = view.findViewById(R.id.btnAddNewFolder);
            Button cancelButton = view.findViewById(R.id.btnCancel);
            final EditText edtFolderName;

            edtFolderName=view.findViewById(R.id.edtFolderName);
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();



            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onClick: accept button");
                    if(edtFolderName.getText().toString().length()<1)
                    {
                        edtFolderName.setError("Can't be empty");
                        edtFolderName.setFocusable(true);
                    }else
                    {
                        String folderName= edtFolderName.getText().toString().trim();
                        Log.d(TAG, "onClick: started final text= "+folderName);
                        alertDialog.dismiss();
                        addToDatabase(folderName);


                    }


                }
            });





            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onClick: cancel button" );

                    alertDialog.dismiss();
                }
            });

            alertDialog.show();



        }

    private void addToDatabase(String folderName) {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a= YEAR*100+MONTH;
        a=a*100+DATE;
        String date = DateFormat.format("dd.MM.yy", calendar).toString();
        String name= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Folder folder= new Folder(folderName,name,date,folderName,a);
        ref.add(folder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: "+documentReference.toString());
                Toast.makeText(ResourcesActivity.this, "Folder Created", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getLocalizedMessage();

            }
        });


    }


    private void setupRecView() {
        Query query= ref.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Folder> options= new FirestoreRecyclerOptions.Builder<Folder>()
                .setQuery(query,Folder.class)
                .build();
        adapter= new FolderAdapter(options);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
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