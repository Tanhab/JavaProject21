package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The class for Profile activity.
 */
public class ProfileActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
private static final String TAG = "ProfileActivity";

    /**
     * The EditText for nickname.
     */
private EditText txtNickname;
    /**
     * The EditText for name.
     */
private EditText txtName;
    /**
     * The EditText for phn no.
     */
private EditText txtPhnNo;
    /**
     * The EditText for address.
     */
private EditText txtAddress;
    /**
     * The EditText for bio.
     */
private EditText txtBio;
    /**
     * The EditText for hobbies.
     */
private EditText txtHobbies;
    /**
     * The TextView for date of birth.
     */
private TextView txtDateOfBirth;
    /**
     * The Button for open gallery.
     */
private Button btnOpenGallery;
    /**
     * The Button for save profile.
     */
private Button btnSaveProfile;
    /**
     * The CircleImageView for Profile pic.
     */
private CircleImageView profilePic;
    /**
     * The ImageButton for Date button.
     */
private ImageButton dateButton;
    /**
     * The spinner for district.
     */
private Spinner districtSpinner;
    /**
     * The spinner for Blood group.
     */
private Spinner bloodGroupSpinner;
    /**
     * The uri for image.
     */
private Uri imageUri=null;
    /**
     * The string for Past url.
     */
    String pastUrl;
    /**
     * The string for Url.
     */
    String url;
    /**
     * The constant variable for IMAGE_REQUEST.
     */
    public static final int IMAGE_REQUEST= 100;
    /**
     * The ProgressDialog variable.
     */
    ProgressDialog pd;
    /**
     * The StorageReference variable.
     */
    StorageReference storageReference;
    /**
     * The String for From activity.
     */
    String fromActivity;
    /**
     * The string array for Districts.
     */
    String[] districts;
    /**
     * The string array for Blood groups.
     */
    String[] bloodGroups;
    /**
     * The String for District name.
     */
    String districtName;
    /**
     * The String for Blood group.
     */
    String bloodGroup;
    /**
     * The map for District key.
     */
    Map<String,Integer> districtKey;
    /**
     * The map for Blood key.
     */
    Map<String,Integer> bloodKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtName= findViewById(R.id.txtName);
        txtNickname= findViewById(R.id.txtNickname);
        txtAddress= findViewById(R.id.txtAddress);
        txtBio= findViewById(R.id.txtBio);
        txtHobbies=findViewById(R.id.txtHobbies);
        txtPhnNo=findViewById(R.id.txtPhnNo);
        txtDateOfBirth=findViewById(R.id.txtDate);
        btnOpenGallery=findViewById(R.id.btnOpenGallery);
        btnSaveProfile=findViewById(R.id.btnSaveProfile);
        profilePic=findViewById(R.id.profilePic);
        dateButton=findViewById(R.id.dateBtn);
        districtSpinner=findViewById(R.id.districtSpinner);
        districtKey= new HashMap<>();
        bloodKey= new HashMap<>();
        bloodGroupSpinner=findViewById(R.id.bloodGroupSpinner);
        pd = new ProgressDialog(this);
        districtName="";
        bloodGroup="";

        districts=getResources().getStringArray(R.array.districts);
        Arrays.sort(districts);
        for (int i = 0; i < districts.length; i++) {
            districtKey.put(districts[i],i);
        }
        bloodGroups=getResources().getStringArray(R.array.bloodGroup);
        for (int i = 0; i < bloodGroups.length; i++) {
            bloodKey.put(bloodGroups[i],i);
        }

        pd.setCancelable(false);

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,R.layout.sample_layout_spinner,R.id.txtSpinnerId,districts){

           /* @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){

                    tv.setTextColor(Color.DKGRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }*/
        };
        //adapter.setDropDownViewResource(R.layout.sample_layout_spinner);
        districtSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(this,R.layout.sample_layout_spinner,R.id.txtSpinnerId,bloodGroups){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };
        bloodGroupSpinner.setAdapter(adapter1);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        Intent intent= getIntent();
        if(intent.hasExtra("fromActivity"))
        {
            fromActivity= intent.getStringExtra("fromActivity");
        }



        storageReference= FirebaseStorage.getInstance().getReference();
        checkForData();

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Sorry,due to server problem dp upload is temporarily off.Give me in messenger i will add it. ", Toast.LENGTH_SHORT).show();

            }
        });
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                districtName=districtSpinner.getSelectedItem().toString();
                bloodGroup=bloodGroupSpinner.getSelectedItem().toString();

                if(txtNickname.getText().toString().length()<1)
                {
                    txtNickname.setError("Can't be empty");
                }else if(txtName.getText().toString().length()<1)
                {
                    txtName.setError("Can't be empty");
                }else if(txtAddress.getText().toString().length()<1)
                {
                    txtAddress.setError("Can't be empty");
                }else if(districtName.length()<1)
                {
                    Toast.makeText(ProfileActivity.this, "Hometown must be chosen", Toast.LENGTH_SHORT).show();
                }
                else {
                    pd.setTitle("Updating Profile...");
                    pd.show();
                    Log.d(TAG, "onClick: started save profile");
                    saveProfilePic();
                }
            }
        });



    }

    /**
     * This method calls a method to resize the image when image has been
     * set otherwise calls another method to add other info the database.
     */
private void saveProfilePic()  {
        String imageName= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "saveProfilePic: started" +imageUri);

        if(imageUri!=null)
        {
            StorageReference ref= storageReference.child(imageName+"."+getFileExtension(imageUri));
            Log.d(TAG, "saveProfilePic: started imageuri ! null");
            BackgroundImageResize backgroundImageResize=new BackgroundImageResize(null);
            backgroundImageResize.execute(imageUri);
        }else {
            //TODO: check for other Info
            Uri uri= Uri.parse(pastUrl);
            addToDatabase(uri);
        }


    }

    /**
     * This method adds all the info given by the user in the profile activity
     * in the database.
     *
     * @param uri the uri of the image
     */
private void addToDatabase(Uri uri) {
        Log.d(TAG, "addToDatabase: started");
        String mname = txtName.getText().toString().trim();
        String nickName=txtNickname.getText().toString().trim();
        String address=txtAddress.getText().toString().trim();
        String bio=txtBio.getText().toString().trim();
        String hobbies=txtHobbies.getText().toString().trim();
        String phnNo= txtPhnNo.getText().toString().trim();
        String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Map<String,Object> map= new HashMap<>()
        ;//String name,PhoneNo,BloodGroup,Email,imageUri,nickName,address,bio,district,hobbies,dateOfBirth;
        map.put("name",mname);
        map.put("nickName",nickName);
        map.put("address",address);
        if(bio.length()>0)
            map.put("bio",bio);
        else map.put("bio","");
        if(hobbies.length()>0)
            map.put("hobbies",hobbies);
        map.put("district",districtName);
        map.put("BloodGroup",bloodGroup);
        map.put("PhoneNo",phnNo);
        map.put("dateOfBirth",txtDateOfBirth.getText().toString());
        if(uri!=null){
            url= uri.toString();
        }else  url="";
        map.put("imageUri",url);
        map.put("Email",email);
        map.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Student student= new Student(mname,phnNo,bloodGroup,email,url,nickName,address,bio,districtName,hobbies,txtDateOfBirth.getText().toString());
        db.collection("Users").document(email)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Started : DocumentSnapshot successfully written!");

                        pd.dismiss();
                        Log.d(TAG, "onSuccess: fromActivity " + fromActivity);
                        if(fromActivity==null) {
                            startActivity(new Intent(getApplicationContext(), ChooseClassActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        pd.dismiss();
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    /**
     * This method handles the date button by creating date picker dialog.
     */
private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
                txtDateOfBirth.setText(dateText);

            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();


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

    /**
     * This method opens the file chooser for image selection.
     */
private void openfileChooser() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    /**
     * This method gets the file extension of the image uploaded.
     *
     * @param imageUri the image uri
     * @return the file extension of the image
     */
    public String getFileExtension(Uri imageUri)
    {
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }

    /**
     * The class for Background image resize.
     */
    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        /**
         * The M bitmap.
         */
        Bitmap mBitmap;

        /**
         * Instantiates a new Background image resize.
         *
         * @param bitmap the bitmap
         */
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
            final String name=FirebaseAuth.getInstance().getCurrentUser().getUid()+"."+getFileExtension(imageUri);
            StorageReference ref= storageReference.child(name);


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

    /**
     * This method gets bytes from bitmap of the image.
     *
     * @param bitmap  the bitmap
     * @param quality the quality
     * @return the byte [ ]
     */
public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * This method handles the showing all the information and image of the user
     * when entered into the profile page in presence of data connection.
     */
private void checkForData() {
        pd.setTitle("Please wait..");
        pd.show();
        String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();


        FirebaseFirestore.getInstance().collection("Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Student student= documentSnapshot.toObject(Student.class);
                        assert student != null;
                        txtName.setText(student.getName());
                        txtNickname.setText(student.getNickName());
                        txtAddress.setText(student.getAddress());
                        txtBio.setText(student.getBio());
                        txtHobbies.setText(student.getHobbies());
                        bloodGroup=student.getBloodGroup();
                        districtName=student.getDistrict();
                        int i=-1;
                        if(districtKey.containsKey(districtName)&& districtName!=null) i=districtKey.get(districtName);
                        if(i>=0&&i<=63) {
                            districtSpinner.setSelection(i);
                        }
                        int j=-1;
                        if(bloodKey.containsKey(bloodGroup)&& bloodGroup!=null) j=bloodKey.get(bloodGroup);
                        if(j>=0&&j<=10) {
                            bloodGroupSpinner.setSelection(j);
                        }

                        if(student.getDateOfBirth()!=null) txtDateOfBirth.setText(student.getDateOfBirth());
                        txtPhnNo.setText(student.getPhoneNo());
                        pastUrl=student.getImageUri();
                        Glide.with(getApplicationContext()).load(student.getImageUri()).placeholder(R.drawable.classicon).into(profilePic);
                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "Error fetching profile data.Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
