package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private EditText txtName,txtPhnNo,txtBloodGroup;
    private Button btnOpenGallery,btnSaveProfile;
    private CircleImageView profilePic;
    private Uri imageUri;
    Bitmap thumbBitmap=null;
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
        pd.setCancelable(false);



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
            Uri resultUri= imageUri;
            BackgroundImageResize backgroundImageResize=new BackgroundImageResize(null);
            backgroundImageResize.execute(imageUri);
          
        }else {
            //TODO: check for other Info
                Uri uri= null;
            addToDatabase(uri);
        }


    }

    private void addToDatabase(Uri uri) {
        String name = txtName.getText().toString().trim();
        String bloodGrp= txtBloodGroup.getText().toString().trim().toUpperCase();
        String phnNo= txtPhnNo.getText().toString().trim();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String,Object> map= new HashMap<>();
        map.put("name",""+name);
        map.put("BloodGroup",""+bloodGrp);
        map.put("PhoneNo",""+phnNo);
        map.put("imageUri",""+uri);
        map.put("Email",""+email);
        map.put("Uid",""+FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(email)
                .update(map)
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
    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if(bitmap != null){
                this.mBitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Toast.makeText(getApplicationContext(), "compressing image", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPreExecute: Compressing started");
            pd.show();

        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started.");

            if(mBitmap == null){
                try{
                    mBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), params[0]);
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: IOException: " + e.getMessage());
                }
            }
            byte[] bytes = null;
            bytes = getBytesFromBitmap(mBitmap, 30);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            //mUploadBytes = bytes;
            StorageReference ref= storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"."+getFileExtension(imageUri));

            UploadTask uploadTask= ref.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                    pd.dismiss();
                    exception.getLocalizedMessage();

                }
            });

            //execute the upload task

        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

}
