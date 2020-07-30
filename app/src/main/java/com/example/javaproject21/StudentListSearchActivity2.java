package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentListSearchActivity2 extends AppCompatActivity {
    private static final String TAG ="StudentList" ;
    private Chip nameChip, districtChip, bloogGrpChip;
    private Button btnSearch;
    private ChipsInput districtChipInput, bloodGroupChipInput;
    private String[] districts, bloodgrps;
    private String nameS="";
    private TextInputEditText textInputEditText;
    private TextInputLayout textField;
    private RecyclerView recyclerView;
    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_search2);
        nameChip = findViewById(R.id.chipNameId);
        districtChip = findViewById(R.id.chipDistrictId);
        bloogGrpChip = findViewById(R.id.chipBloodGrpId);

        districtChipInput = findViewById(R.id.chips_district_id);
        bloodGroupChipInput = findViewById(R.id.chips_bloodgrp_id);
        btnSearch = findViewById(R.id.button);
        textField=findViewById(R.id.textfieldId);
        textInputEditText=findViewById(R.id.nameId);

        recyclerView=findViewById(R.id.result_list);
        btnBack=findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupRecView();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textField.setVisibility(View.GONE);
        textInputEditText.setVisibility(View.GONE);
        districtChipInput.setVisibility(View.GONE);
        bloodGroupChipInput.setVisibility(View.GONE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    searchStudents();
                }catch (Exception e)
                {
                    Log.d(TAG, "onClick: " + e);
                }
            }
        });
        districts = getResources().getStringArray(R.array.districts);
        bloodgrps = getResources().getStringArray(R.array.bloodGroup);

        final List<DistrictChips> districtChipsList = new ArrayList<>();
        for (String i : districts) {
            districtChipsList.add(new DistrictChips(i));
        }

        List<BloodGrpChips> bloodGrpChipsList = new ArrayList<>();
        for (String i : bloodgrps) {
            bloodGrpChipsList.add(new BloodGrpChips(i));
        }

        districtChipInput.setFilterableList(districtChipsList);
        bloodGroupChipInput.setFilterableList(bloodGrpChipsList);

        nameChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textField.setVisibility(View.VISIBLE);
                    textInputEditText.setVisibility(View.VISIBLE);
                }
                else
                {
                    textInputEditText.setText("");
                    textField.setVisibility(View.GONE);
                    textInputEditText.setVisibility(View.GONE);
                }
            }
        });
        districtChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    districtChipInput.setVisibility(View.VISIBLE);
                }
                else
                {
                    List<DistrictChips> districtSelectedChipsList = (List<DistrictChips>)districtChipInput.getSelectedChipList();

                    for(DistrictChips tem: districtSelectedChipsList){
                        districtChipInput.removeChipByInfo(tem.getInfo());
                    }

                    districtChipInput.setVisibility(View.GONE);
                }
            }
        });
        bloogGrpChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    bloodGroupChipInput.setVisibility(View.VISIBLE);
                }
                else
                {

                    List<BloodGrpChips> bloodGroupSelectedChipsList = (List<BloodGrpChips>)bloodGroupChipInput.getSelectedChipList();

                    for(BloodGrpChips tem: bloodGroupSelectedChipsList){
                        bloodGroupChipInput.removeChipByInfo(tem.getInfo());
                    }
                    bloodGroupChipInput.setVisibility(View.GONE);
                }
            }
        });
    }
// THIS IS INCOMPLETE
    private void searchStudents() {

        //incase name diye search dile ekhane ashbe
        nameS=textInputEditText.getText().toString().trim();

        //incase district/blood group diye search dile ekhane ashbe
        List<DistrictChips> districtSelectedChipsList = (List<DistrictChips>)districtChipInput.getSelectedChipList();
        List<BloodGrpChips> bloodGroupSelectedChipsList = (List<BloodGrpChips>)bloodGroupChipInput.getSelectedChipList();

        List<String> districtSelectedChipsStringList = new ArrayList<>();
        List<String> bloodGroupSelectedChipsStringList = new ArrayList<>();

        for(DistrictChips t: districtSelectedChipsList){
            districtSelectedChipsStringList.add(t.getInfo());
        }
        if(bloodGroupSelectedChipsList.size() == 0){

            for(String i: bloodgrps)
            {
                bloodGroupSelectedChipsStringList.add(i);
            }

        }else
        {
            for(BloodGrpChips t: bloodGroupSelectedChipsList)
            {
                bloodGroupSelectedChipsStringList.add(t.getInfo());
            }
        }
// whereIn diye try korechilm bt wherein matro ekta deya jay...so i failed
        Query query;
        if(districtSelectedChipsStringList.isEmpty()){
            query = FirebaseFirestore.getInstance().collection("Users")
                    .whereEqualTo("currentClass",Utils.getClassName())
                    .whereIn("BloodGroup", bloodGroupSelectedChipsStringList);
        }
        else {
            query = FirebaseFirestore.getInstance().collection("Users")
                    .whereEqualTo("currentClass",Utils.getClassName())
                    .whereIn("BloodGroup", bloodGroupSelectedChipsStringList)
                    .whereIn("district",districtSelectedChipsStringList );
        }

        FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class).build();


        FirestoreRecyclerAdapter<Student, UsersViewHolder> studentSearchAdapter= new FirestoreRecyclerAdapter<Student,UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull Student model) {
                holder.setDetails(getApplicationContext(),model.getName(),model.getBio(),model.getImageUri());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                        showStudentInfo(snapshot);

                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_item,parent,false);
                return new UsersViewHolder(view);
            }
        };

        recyclerView.setAdapter(studentSearchAdapter);
        studentSearchAdapter.startListening();

    }

    private void setupRecView() {
        Query query= FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass",Utils.getClassName());

        FirestoreRecyclerOptions<Student> options=new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query,Student.class)
                .build();

        FirestoreRecyclerAdapter<Student, UsersViewHolder> studentSearchAdapter= new FirestoreRecyclerAdapter<Student, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, final int position, @NonNull Student model) {
                holder.setDetails(getApplicationContext(),model.getName(),model.getBio(),model.getImageUri());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                        showStudentInfo(snapshot);

                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_item,parent,false);
                return new UsersViewHolder(view);
            }
        };
        recyclerView.setAdapter(studentSearchAdapter);
        studentSearchAdapter.startListening();

    }


    /**
     * The holder class of the adapter which extends RecyclerView.ViewHolder .
     */
    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        /**
         * The view variable.
         */
        View mView;

        /**
         * Instantiates a new Users view holder.
         *
         * @param itemView the item view
         */
        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        /**
         * This method sets the details of a user to the holder of the recycler adapter.
         *
         * @param ctx        the context
         * @param userName   the user name
         * @param userStatus the user status
         * @param userImage  the user image
         */
        public void setDetails(Context ctx, String userName, String userStatus, String userImage ){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).placeholder(R.drawable.prof).into(user_image);

        }




    }


    private void showStudentInfo(DocumentSnapshot snapshot) {
        Student student=snapshot.toObject(Student.class);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.show_student_info_dialog, null);

        TextView txtName,txtEmail,txtBlood,txtPhoneNo,txtNickName,txtHobbies,txtAddress,txtBDay,txtHomeTown;
        CircleImageView profilePic;
        txtBlood=view.findViewById(R.id.txtBloodGroup);
        txtEmail=view.findViewById(R.id.txtEmail);
        txtName=view.findViewById(R.id.txtName);
        txtPhoneNo=view.findViewById(R.id.txtPhoneNo);
        profilePic=view.findViewById(R.id.profilePic);
        txtAddress=view.findViewById(R.id.txtAddress);
        txtBDay=view.findViewById(R.id.txtBirthday);
        txtHobbies=view.findViewById(R.id.txtHobbies);
        txtNickName=view.findViewById(R.id.txtNickname);
        txtHomeTown=view.findViewById(R.id.txtHometown);


        if(student!=null) {
            if (student.getBloodGroup() != null) txtBlood.setText(student.getBloodGroup());
            if (student.getEmail() != null) txtEmail.setText(student.getEmail());
            if (student.getName() != null) txtName.setText(student.getName());
            if (student.getPhoneNo() != null) txtPhoneNo.setText(student.getPhoneNo());
            if(student.getAddress()!=null) txtAddress.setText(student.getAddress());
            if(student.getDistrict()!=null) txtHomeTown.setText(student.getDistrict());
            if(student.getDateOfBirth()!=null) txtBDay.setText(student.getDateOfBirth());
            if(student.getNickName()!=null) txtNickName.setText(student.getNickName());
            if(student.getHobbies()!=null) txtHobbies.setText(student.getHobbies());

            Glide.with(view).load(student.getImageUri()).placeholder(R.drawable.prof).into(profilePic);
        }


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


//egula chipInput implement er jonno legeche
class DistrictChips implements ChipInterface {

    String district;

    public DistrictChips(String district) {
        this.district = district;
    }

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return district;
    }

    @Override
    public String getInfo() {
        return district.trim();
    }
}

class BloodGrpChips implements ChipInterface {

    String bloodGroup;

    public BloodGrpChips(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return bloodGroup;
    }

    @Override
    public String getInfo() {
        return bloodGroup.trim();
    }
}


