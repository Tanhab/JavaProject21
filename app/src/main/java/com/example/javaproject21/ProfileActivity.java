package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private EditText txtName,txtPhnNo,txtBloodGroup;
    private Button btnOpenGallery,btnSaveProfile;
    private CircleImageView profilePic;
    private Uri imageUri;
    public static final int IMAGE_REQUEST= 100;
    ProgressDialog pd;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtName= findViewById(R.id.txtName);
        txtPhnNo=findViewById(R.id.txtPhnNo);
        txtBloodGroup=findViewById(R.id.txtBloodGroup);
        btnOpenGallery=findViewById(R.id.btnOpenGallery);
        btnSaveProfile=findViewById(R.id.btnSaveProfile);
        profilePic=findViewById(R.id.profilePic);
        pd = new ProgressDialog(this);
        pd.setTitle("Updating Profile...");


        storageReference= FirebaseStorage.getInstance().getReference();



        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfileChooser();
            }
        });
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                Log.d(TAG, "onClick: started save profile");
                saveProfilePic();
            }
        });



    }

    private void saveProfilePic()  {
        String imageName= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "saveProfilePic: started" +imageUri);

        StorageReference ref= storageReference.child(imageName+"."+getFileExtension(imageUri));
        if(imageUri!=null)
        {
            Log.d(TAG, "saveProfilePic: started imageuri ! null");




            /*final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+imageName+"."+getFileExtension(imageUri));
            UploadTask uploadTask = ref.putFile(imageUri);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        addToDatabase(downloadUri);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });*/

            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Log.d(TAG, "onSuccess: started image upload done");
                            //String uri= taskSnapshot.getStorage().getDownloadUrl().toString();
                            //addToDatabase(uri);
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "onSuccess: started got uri "+ uri);
                                    addToDatabase(uri);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.getLocalizedMessage();
                                    e.printStackTrace();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            pd.dismiss();
                            exception.getLocalizedMessage();

                        }
                    });


        }else {
            //TODO: check for other Info
                Uri uri= null;
            addToDatabase(uri);
        }


    }

    private void addToDatabase(Uri uri) {
        String name = txtName.getText().toString().trim();
        String bloodGrp= txtBloodGroup.getText().toString().trim();
        String phnNo= txtPhnNo.getText().toString().trim();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("name",""+name);
        map.put("BloodGroup",""+bloodGrp);
        map.put("PhoneNo",""+phnNo);
        //map.put("imageUri",""+uri);
        map.put("Email",""+email);
        map.put("Uid",""+FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(email)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Started : DocumentSnapshot successfully written!");
                        pd.dismiss();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        pd.dismiss();
                        e.getLocalizedMessage();
                    }
                });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST&& resultCode==RESULT_OK&& data!= null && data.getData()!= null)
        {
            imageUri= data.getData();
            Glide.with(this).load(imageUri).placeholder(R.drawable.ic_add_profile_pic).into(profilePic);
        }
    }

    private void openfileChooser() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    public String getFileExtension(Uri imageUri)
    {
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }
}
