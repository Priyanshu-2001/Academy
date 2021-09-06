package com.sports.oscaracademy.drawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.FragmentProfileBinding;
import com.sports.oscaracademy.service.studentsList;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final String UID = "param1";
    private static final String EDITABLE = "editable";
    private String userID;
    private String editable = "false";
    private SharedPreferences prefs;
    private final MutableLiveData<Studentdata> currentStudent = new MutableLiveData<>();
    private String currentSelection;
    private FragmentProfileBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ProfileFragment() {
    }


    public static ProfileFragment newInstance(String userID, String editable) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(UID, userID);
        args.putString(EDITABLE, editable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(UID);
            try {
                editable = getArguments().getString(EDITABLE);
            } catch (Exception e) {
                editable = "false";
            }
        }
        prefs = requireActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("Profile", "onCreateView: " + editable);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        if (editable.equals("false")) { // list of current student is shown
            binding.deleteStudent.setVisibility(View.VISIBLE);
        }
        if (editable.equals("true")) { // if its in add student sectoin i.e. users list is shown
            binding.addStudent.setVisibility(View.VISIBLE);
//                    binding.feesVaildity.setEnabled(true);
        }
        if (editable.equals("user")) {
            //if clicked in profile
        }
        Sprite doubleBounce = new WanderingCubes();
        binding.progress.setIndeterminateDrawable(doubleBounce);

        List<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Prefer Not To Say");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, gender);
        binding.studentGender.setAdapter(arrayAdapter);
        binding.studentGender.setThreshold(0);
        binding.studentGender.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.text_field_1));

        binding.progress.setVisibility(View.GONE);
        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editbtn.setVisibility(View.GONE);
                binding.savebtn.setVisibility(View.VISIBLE);
                if (editable.equals("false") || editable.equals("true")) {
                    enableAll();
                }
                if (editable.equals("user")) {
                    enableAll();
                    disableAdminEditable();
                }
            }
        });
        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editbtn.setVisibility(View.VISIBLE);
                binding.savebtn.setVisibility(View.GONE);
                UpdateDetails(prefs.getString("isStudent", "false"));
                disableAll();
            }
        });
        binding.addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AddStudentToAcademy();
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Unable To Add Student", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        binding.deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudents();
            }
        });

        //editable showing
        String isStudent = prefs.getString("isStudent", "false");
        Log.e("TAG", "onCreateView: " + isStudent);
        studentsList list = new studentsList(getActivity());

        String role = prefs.getString("role", "0"); //(role.equals("1")) is admin
        if ((isStudent.equals("true") && prefs.getString("role", "0").equals("0") || editable.equals("false"))) {
            list.getStudents().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    Log.d("TAG", "onChanged: req " + userID);
                    for (int i = 0; i < studentdata.size(); i++) {
                        Log.d("TAG", "onChanged: got " + studentdata.get(i).getUserId());
                        if (studentdata.get(i).getUserId().equals(userID)) {
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getName());
                            binding.setModel(studentdata.get(i));
                            currentStudent.setValue(studentdata.get(i));
                            currentSelection = "student";
                        }
                    }
                }
            });
        } else {
            list.getUsers().observe(requireActivity(), new Observer<ArrayList<Studentdata>>() {
                @Override
                public void onChanged(ArrayList<Studentdata> studentdata) {
                    Log.d("TAG", "onChanged: req " + userID);
                    for (int i = 0; i < studentdata.size(); i++) {
                        Log.d("TAG", "onChanged: got " + studentdata.get(i).getUserId());

                        if (studentdata.get(i).getUserId().equals(userID)) {
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                            Log.e("TAG", "onChanged: " + studentdata.get(i).getPhone());
                            binding.setModel(studentdata.get(i));
                            disableAll();
                            currentStudent.setValue(studentdata.get(i));
                            currentSelection = "user";
                        }
                    }
                }
            });
        }
        binding.savebtn.setVisibility(View.GONE);

        currentStudent.observe(requireActivity(), new Observer<Studentdata>() {
            @Override
            public void onChanged(Studentdata studentdata) {
                binding.progress.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }

    private void UpdateDetails(String isStudent) {
        binding.progress.setVisibility(View.VISIBLE);
        studentsList service = new studentsList(getActivity());
        service.updateProfile(currentSelection, binding.progress, currentStudent.getValue().getUserId(), getDataFromTxtViews());
    }

    private void deleteStudents() {
        Map<String, Object> data = getDataFromTxtViews();
        displayConfirmationDialog("DELETE", "Please Connfirm Your Deletion", getContext(), data);

    }

    public Map<String, Object> getDataFromTxtViews() {
        Map<String, Object> ProfileData = new HashMap<>();
        ProfileData.put("Age", binding.studentAge.getText().toString().trim());
        ProfileData.put("name", binding.StudentName.getText().toString().trim());
        ProfileData.put("email", binding.studentMail.getText().toString().trim());
        ProfileData.put("Sex", binding.studentGender.getText().toString().trim());
//        ProfileData.put("Dob",binding.studentDOB);
        ProfileData.put("userID", currentStudent.getValue().getUserId());
        ProfileData.put("Phone Number", binding.phoneNumber.getText().toString().trim());
        ProfileData.put("RollNo", Integer.valueOf(binding.StudentRollNo.getText().toString().trim()));
        ProfileData.put("joinedTill", binding.StudentMember.getText().toString().trim());
        ProfileData.put("session", binding.studentSession.getText().toString().trim());
        return ProfileData;
    }

///

    private void AddStudentToAcademy() throws ParseException {
        binding.progress.setVisibility(View.VISIBLE);
        Map<String, Object> ProfileData = new HashMap<>();
        ProfileData.put("Age", binding.studentAge.getText().toString().trim());
        ProfileData.put("name", binding.StudentName.getText().toString().trim());
        ProfileData.put("email", binding.studentMail.getText().toString().trim());
        ProfileData.put("Sex", binding.studentGender.getText().toString().trim());
//        ProfileData.put("Dob",binding.studentDOB);
        ProfileData.put("phone number", binding.phoneNumber.getText().toString().trim());
        ProfileData.put("userID", currentStudent.getValue().getUserId());
        Map<String, Object> validity = new HashMap<>();
        validity.put("valid from", Timestamp.now());
        ProfileData.put("session", binding.studentSession.getText().toString().trim());
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Log.e("TAG", "AddStudentToAcademy: " + binding.StudentMember.getText());
        Date date = format.parse(String.valueOf(binding.StudentMember.getText()));
        validity.put("valid to", new Timestamp(date));
        ProfileData.put("fees", validity);
        if (binding.StudentRollNo.getText().toString().isEmpty()) {
            studentsList service = new studentsList(getContext());
            service.getRoll(userID).observe(requireActivity(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer s) {
                    ProfileData.put("RollNo", s);
                    studentAdder(ProfileData);
                }
            });
        } else {
            ProfileData.put("RollNo", Integer.valueOf(binding.StudentRollNo.getText().toString().trim()));
            studentAdder(ProfileData);
        }
    }

    public final void displayConfirmationDialog(String title, String str, Context context, Map<String, Object> data) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setMessage(str);
        builder.setTitle(title);
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            studentsList service = new studentsList(getActivity());
            service.deleteStudent(currentStudent.getValue().getUserId(), data);

        });
        builder.setNegativeButton("Dismiss", (dialog, which) -> {
            Log.e("TAG", "displayConfirmationDialog: Canceled");
        });
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
    }

    public void studentAdder(Map<String, Object> ProfileData) {
        Map<String, Object> Profile = new HashMap<>();
        Profile.put("isStudent", "true");
        firestore.collection("user").document(userID).set(Profile, SetOptions.merge());
        firestore.collection("students").document(userID).set(ProfileData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Student Added SucessFully", Toast.LENGTH_LONG).show();
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                                requireActivity().finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    Log.e("TAG", "onComplete: eroor in adding student " + task.getException());
                    Toast.makeText(getContext(), "Some Error Occured " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                binding.progress.setVisibility(View.GONE);
            }
        });
    }


    public void disableAdminEditable() {
        binding.studentMail.setEnabled(false);
        binding.phoneNumber.setEnabled(false);
        binding.studentSession.setEnabled(false);
        binding.StudentRollNo.setEnabled(false);
        binding.StudentMember.setEnabled(false);
    }

    public void enableAll() {
        binding.StudentName.setEnabled(true);
        binding.studentMail.setEnabled(true);
        binding.phoneNumber.setEnabled(true);
        binding.studentMail.setEnabled(true);
        binding.studentGender.setEnabled(true);
        binding.studentAge.setEnabled(true);
        binding.studentSession.setEnabled(true);
        binding.StudentRollNo.setEnabled(true);
        binding.StudentMember.setEnabled(true);
    }

    public void disableAll() {
        binding.StudentName.setEnabled(false);
        binding.studentMail.setEnabled(false);
        binding.phoneNumber.setEnabled(false);
        binding.studentMail.setEnabled(false);
        binding.studentGender.setEnabled(false);
        binding.studentAge.setEnabled(false);
        binding.studentSession.setEnabled(false);
        binding.StudentRollNo.setEnabled(false);
        binding.StudentMember.setEnabled(false);
    }
}