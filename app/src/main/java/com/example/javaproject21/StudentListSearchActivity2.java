package com.example.javaproject21;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentListSearchActivity2 extends AppCompatActivity {
    private static final String TAG = "StudentList";
    private Chip nameChip, districtChip, bloogGrpChip;
    private Button btnSearch;
    private ChipsInput districtChipInput, bloodGroupChipInput;
    private String[] districts, bloodgrps;
    private String nameS = "";
    private TextInputEditText textInputEditText;
    private TextInputLayout textField;
    private RecyclerView recyclerView;
    static List<Student> fullList;
    List<Student> showList;
    private Context context;
    private StudentSearchManualAdapter manualAdapter;
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
        textField = findViewById(R.id.textfieldId);
        textInputEditText = findViewById(R.id.nameId);
        context = getApplicationContext();

        recyclerView = findViewById(R.id.result_list);
        btnBack = findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setupRecView();
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


        fullList = new ArrayList<>();
        Query query = FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass", Utils.getClassName())
                .orderBy("name", Query.Direction.ASCENDING);

        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass", Utils.getClassName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                Student s = document.toObject(Student.class);
                                Log.d(TAG, "student : " + s.toString());
                                fullList.add(s);
                                Log.d(TAG, "fulllist update " + fullList.size());
                            }
                            showList = new ArrayList<>();
                            showList = fullList;
                            manualAdapter = new StudentSearchManualAdapter(showList);
                            recyclerView.setAdapter(manualAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchStudents();
                } catch (Exception e) {
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
                if (isChecked) {
                    textField.setVisibility(View.VISIBLE);
                    textInputEditText.setVisibility(View.VISIBLE);
                } else {
                    textInputEditText.setText("");
                    textField.setVisibility(View.GONE);
                    textInputEditText.setVisibility(View.GONE);
                }
            }
        });
        districtChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    districtChipInput.setVisibility(View.VISIBLE);
                } else {
                    List<DistrictChips> districtSelectedChipsList = (List<DistrictChips>) districtChipInput.getSelectedChipList();

                    for (DistrictChips tem : districtSelectedChipsList) {
                        districtChipInput.removeChipByInfo(tem.getInfo());
                    }

                    districtChipInput.setVisibility(View.GONE);
                }
            }
        });
        bloogGrpChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bloodGroupChipInput.setVisibility(View.VISIBLE);
                } else {

                    List<BloodGrpChips> bloodGroupSelectedChipsList = (List<BloodGrpChips>) bloodGroupChipInput.getSelectedChipList();

                    for (BloodGrpChips tem : bloodGroupSelectedChipsList) {
                        bloodGroupChipInput.removeChipByInfo(tem.getInfo());
                    }
                    bloodGroupChipInput.setVisibility(View.GONE);
                }
            }
        });
    }

    // THIS IS INCOMPLETE
    private void updateFullList() {
        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("currentClass", Utils.getClassName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                Student s = document.toObject(Student.class);
                                Log.d(TAG, "student : " + s.toString());
                                fullList.add(s);
                                Log.d(TAG, "fulllist update " + fullList.size());
                            }
                            searchStudents();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void searchStudents() {

        //incase name diye search dile ekhane ashbe
        nameS = Objects.requireNonNull(textInputEditText.getText()).toString().trim().toLowerCase();

        //incase district/blood group diye search dile ekhane ashbe
        List<DistrictChips> districtSelectedChipsList = (List<DistrictChips>) districtChipInput.getSelectedChipList();
        List<BloodGrpChips> bloodGroupSelectedChipsList = (List<BloodGrpChips>) bloodGroupChipInput.getSelectedChipList();

        List<String> districtSelectedChipsStringList = new ArrayList<>();
        List<String> bloodGroupSelectedChipsStringList = new ArrayList<>();

        for (DistrictChips t : districtSelectedChipsList) {
            districtSelectedChipsStringList.add(t.getInfo());
        }


        {
            for (BloodGrpChips t : bloodGroupSelectedChipsList) {
                bloodGroupSelectedChipsStringList.add(t.getInfo());
            }
        }
        Log.d(TAG, "searchStudents: bloodgroup " + bloodGroupSelectedChipsStringList.toString());
        Log.d(TAG, "searchStudents: district " + districtSelectedChipsStringList.toString());
        Log.d(TAG, "searchStudents: fullList " + fullList.toString());
        for (Student s : fullList) {
            Log.d(TAG, "Student " + s.district);
        }
        List<Student> temp = new ArrayList<>();
        if (nameS.length() == 0 && bloodGroupSelectedChipsStringList.size() == 0 && districtSelectedChipsStringList.size() == 0) {
            temp.addAll(fullList);
        }
        //name
        if (nameS.length() > 0) {
            for (Student s : fullList) {
                String ck = s.name.toLowerCase();
                if (ck.contains(nameS.toLowerCase())) {
                    temp.add(s);
                }

            }
        }
        //district
        Log.d(TAG, "fulllist size = " + fullList.size());
        Log.d(TAG, "districtlist size = " + districtSelectedChipsStringList.size());
        for (String d : districtSelectedChipsStringList) {
            for (Student s : fullList) {
                Log.d(TAG, "student district = " + s.district);
                if (d.toLowerCase().contains(s.district.toLowerCase())) {
                    temp.add(s);
                }
            }
        }
        //bloodgroup
        for (String blood : bloodGroupSelectedChipsStringList) {
            for (Student s : fullList) {
                if (blood.equals(s.getBloodGroup())) {
                    temp.add(s);
                }
            }
        }
        Log.d(TAG, "showlist " + temp.toString());
        manualAdapter.update(temp);


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



