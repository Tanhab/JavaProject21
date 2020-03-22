package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leinardi.android.speeddial.FabWithLabelView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Calendar;

public class AllDocumentsActivity extends AppCompatActivity {
    private static final String TAG = "AllDocumentsActivity";
    private static final int ADD_ACTION_POSITION = 4;
    private SpeedDialView mSpeedDialView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref;
    //= db.collection("cse18").document("Data").collection("Folders");
    Query query;
    DocumentAdapter adapter;
    private RecyclerView recyclerView;
    TextView txtFolderName;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_documents);
        txtFolderName=findViewById(R.id.text1);
        Intent intent = getIntent();
        String path = intent.getStringExtra("folderName");
        txtFolderName.setText(path);
        ref = db.collection("cse18").document("Documents").collection(path);
        Log.d(TAG, "onCreate: started intent data " + path);

        initSpeedDial(savedInstanceState == null);
        recyclerView = findViewById(R.id.docRecView);
        setupRecView();


    }

    private void setupRecView() {
        query = ref.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Document> options = new FirestoreRecyclerOptions.Builder<Document>()
                .setQuery(query, Document.class)
                .build();
        adapter = new DocumentAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DocumentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                Document document = documentSnapshot.toObject(Document.class);
                startWebView(document.getUrl());
            }
        });
    }

    private void startWebView(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("Url", url);
        startActivity(intent);
    }

    private void initSpeedDial(boolean addActionItems) {
        mSpeedDialView = findViewById(R.id.speedDial);

        if (addActionItems) {


            Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.paper);
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                    .fab_add_document, drawable)
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, getTheme()))
                    .setLabel("Add a document")
                    .setLabelColor(Color.WHITE)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent,
                            getTheme()))
                    .create());


           /* mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_long_label, R.drawable
                    .ic_sort)
                    .setLabel("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                            "incididunt ut labore et dolore magna aliqua.")
                    .create());*/

            drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_sort);
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_sort_by_date, drawable)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark,
                            getTheme()))
                    .setLabel("Sort by date")
                    .setLabelBackgroundColor(Color.WHITE)
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_sort_by_name, R.drawable
                    .ic_sort)
                    .setLabel("Sort by name")
                    .setLabelBackgroundColor(Color.YELLOW)
                    .create());

        }

        //Set main action clicklistener.
        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {

                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                Log.d(TAG, "Speed dial toggle state changed. Open = " + isOpen);
            }
        });

        //Set option fabs clicklisteners.
        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_add_document:
                        showToast("add a doc pressed :");
                        addDocument();
                        mSpeedDialView.close(); // To close the Speed Dial with animation
                        return false; // false will close it without animation
                    case R.id.fab_sort_by_date:
                        showToast(actionItem.getLabel(getApplicationContext()) + " clicked!");

                        return false;
                    case R.id.fab_sort_by_name:
                        showToast(actionItem.getLabel(getApplicationContext()) + " clicked!\nClosing without animation.");
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)

                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }

    private void addDocument() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_document_dialog, null);

        Button acceptButton = view.findViewById(R.id.btnAddNewDoc);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        final EditText edtDocName, edtUrl;

        edtDocName = view.findViewById(R.id.edtDocName);
        edtUrl = view.findViewById(R.id.edtUrl);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: accept button");
                if (edtDocName.getText().toString().length() < 1) {
                    edtDocName.setError("Can't be empty");
                    edtDocName.setFocusable(true);
                } else if (edtUrl.getText().toString().length() < 1) {
                    edtUrl.setError("Can't be empty");
                    edtUrl.setFocusable(true);
                }
                {
                    String docName = edtDocName.getText().toString().trim();
                    String url = edtUrl.getText().toString().trim();
                    Log.d(TAG, "onClick: started final text= " + url);
                    alertDialog.dismiss();
                    addToDatabase(docName, url);


                }


            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: cancel button");

                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }

    private void addToDatabase(String docName, String url) {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long a = YEAR * 100 + MONTH;
        a = a * 100 + DATE;
        String date = DateFormat.format("dd.MM.yy", calendar).toString();
        String name = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Document document = new Document(docName, date, url, name, a);
        ref.add(document).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: " + documentReference.toString());
                Toast.makeText(AllDocumentsActivity.this, "Doc Created", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getLocalizedMessage();

            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
