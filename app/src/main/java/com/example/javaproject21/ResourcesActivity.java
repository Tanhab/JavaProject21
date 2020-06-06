package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the adding of folder containing
 * pdf of particular subject in resource page and further adding
 * to the database
 */
public class ResourcesActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "ResourcesActivity";
    /**
     * The Recycler view variable.
     */
    RecyclerView recyclerView;
    /**
     * The floating action button variable.
     */
private FloatingActionButton fab;
    /**
     * The FirebaseFirestore variable.
     */
private FirebaseFirestore db= FirebaseFirestore.getInstance();
    /**
     * The CollectionReference variable for folders.
     */
private CollectionReference ref= FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Folders");
    /**
     * The FolderAdapter variable.
     */
private FolderAdapter adapter;
    /**
     * The ProgressDialog variable.
     */
private ProgressDialog pd;
    /**
     * ImageButton variable for going back.
     */
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        recyclerView=findViewById(R.id.folderRecView);
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);
        fab=findViewById(R.id.fab);
        setupRecView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFolder();
            }
        });
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * This method handles the work of adding a new folder by
     * creating an alertDialog and adding to the database
     */
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

    /**
     * This methods updates the new created folder to the
     * database with date and creator name
     * and priority to set the new created folder up front
     *
     * @param folderName the name of the newly created folder
     */
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


    /**
     * This method sets the recycler view of the resource page
     * by creating adapter
     */
private void setupRecView() {
        Query query= ref.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Folder> options= new FirestoreRecyclerOptions.Builder<Folder>()
                .setQuery(query,Folder.class)
                .build();
        adapter= new FolderAdapter(options);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                Folder folder= documentSnapshot.toObject(Folder.class);
                createPath(folder.getFolderName());
                pd.show();
            }
        });
    }

    /**
     * This method helps to create a path in the name of folder in database
     * and sends the user to another page containing
     * all the documents under this folder
     * or a blank page in case of a new folder.
     *
     * @param folderName the name of the folder
     */
private void createPath(final String folderName) {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","default");
        //db.collection(Utils.getClassName()).document("Documents").collection(folderName).document("default").set(map)
        FirebaseFirestore.getInstance().collection("Classrooms").document(Utils.getClassName()).collection("Documents").document("default")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Intent intent= new Intent(getApplicationContext(),AllDocumentsActivity.class);
                        intent.putExtra("folderName",folderName);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ResourcesActivity.this, "Something went wrong.\nPlease check your internet connection.", Toast.LENGTH_SHORT).show();

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
