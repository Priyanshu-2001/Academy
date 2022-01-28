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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Studentdata;
import com.sports.oscaracademy.databinding.FragmentProfileBinding;
import com.sports.oscaracademy.service.studentsList;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String UID = "param1";
    private static final String EDITABLE = "editable";
    private String userID;
    private String editable = "false";
    private SharedPreferences prefs;
    private final MutableLiveData<Studentdata> currentStudent = new MutableLiveData<>();
    private String currentSelection;
    private FragmentProfileBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    studentsList service;

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
        prefs = requireActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        service = new studentsList(requireContext());
        if (getArguments() != null) {
            userID = getArguments().getString(UID);
            try {
                editable = getArguments().getString(EDITABLE);
            } catch (Exception e) {
                editable = "false";
            }
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        if (editable.equals("false")) { // list of current student is shown
            binding.deleteStudent.setVisibility(View.VISIBLE);
        }
        if (editable.equals("true")) { // if its in add student section i.e. users list is shown
            binding.addStudent.setVisibility(View.VISIBLE);
        }

        if (editable.equals("false") || editable.equals("true")) {
            binding.contactStudent.setVisibility(View.VISIBLE);
        }
        service.getSessionsList();
        Sprite doubleBounce = new WanderingCubes();
        binding.progress.setIndeterminateDrawable(doubleBounce);

        List<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Prefer Not To Say");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, gender);
        binding.studentGender.setAdapter(arrayAdapter);
        binding.studentGender.setThreshold(0);
//
        binding.studentGender.setDropDownBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.text_field_1));

        binding.progress.setVisibility(View.GONE);
        binding.editbtn.setOnClickListener(v -> {
            binding.editbtn.setVisibility(View.GONE);
            binding.savebtn.setVisibility(View.VISIBLE);
            if (editable.equals("false") || editable.equals("true")) {
                enableAll();
            }
            if (editable.equals("user")) {
                enableAll();
                disableAdminEditable();
            }
        });
        binding.savebtn.setOnClickListener(v -> {
            binding.editbtn.setVisibility(View.VISIBLE);
            binding.savebtn.setVisibility(View.GONE);
            UpdateDetails(prefs.getString("isStudent", "false"));
            disableAll();
        });
        binding.addStudent.setOnClickListener(v -> {
            try {
                AddStudentToAcademy();
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Unable To Add Student", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        binding.deleteStudent.setOnClickListener(v -> deleteStudents());

        //editable showing
        String isStudent = prefs.getString("isStudent", "false");

        String role = prefs.getString("role", "0"); //(role.equals("1")) is admin
        if ((isStudent.equals("true") && prefs.getString("role", "0").equals("0") || editable.equals("false"))) {
            service.getStudents().observe(requireActivity(), studentdata -> {
                Log.d("TAG", "onChanged: req " + userID);
                for (int i = 0; i < studentdata.size(); i++) {
                    Log.d("TAG", "onChanged: got " + studentdata.get(i).getUserId());
                    if (studentdata.get(i).getUserId().equals(userID)) {
                        Log.e("TAG", "onChanged: " + studentdata.get(i).getUserId());
                        Log.e("TAG", "onChanged: " + studentdata.get(i).getName());
                        binding.setModel(studentdata.get(i));
                        currentStudent.setValue(studentdata.get(i));
                        currentSelection = "student";
                        disableAll();
                    }
                }
            });
        } else {
            service.getUsers().observe(requireActivity(), studentdata -> {
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
            });
        }
        binding.contactStudent.setEnabled(false);
        binding.savebtn.setVisibility(View.GONE);
        currentStudent.observe(requireActivity(), studentdata -> {
            binding.progress.setVisibility(View.GONE);
            try {
                binding.contactStudent.setEnabled(true);
                binding.contactStudent.setOnClickListener(this);
                Log.e("TAG", "onCreateView: membership " + studentdata.getMemberShip());
                service.getSessionsList().observe(this, list -> {
                    list.add("N/A");
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                            (requireContext(), android.R.layout.simple_spinner_item, list);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    binding.session.setAdapter(spinnerArrayAdapter);
                    Log.e("TAG", "onCreateView: " + list.indexOf(studentdata.getSession()) + " " + studentdata.getSession() + list.toString());
                    if (studentdata.getSession() == null || !list.contains(studentdata.getSession())) {
                        binding.session.setSelection(list.size() - 1, true);
                    } else {
                        binding.session.setSelection(list.indexOf(studentdata.getSession()), true);
                    }

                });
                if (studentdata.getPhone() == null || studentdata.getPhone().isEmpty()) {
                    binding.contactStudent.setVisibility(View.GONE);
                }
                setMembershipValidity(studentdata.getMemberShip().trim());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return binding.getRoot();
    }

    private void setMembershipValidity(String validity) {
        binding.feesValidity.setSelection(2, true);
        if (validity.equals("Active")) {
            binding.feesValidity.setSelection(1, true);
        } else {
            binding.feesValidity.setSelection(2, true);

        }
    }

    private void UpdateDetails(String isStudent) {
        binding.progress.setVisibility(View.VISIBLE);
        service.updateProfile(currentSelection, binding.progress, currentStudent.getValue().getUserId(), getDataFromTxtViews());
    }

    private void deleteStudents() {
        Map<String, Object> data = getDataFromTxtViews();
        displayConfirmationDialog("DELETE", "Please Confirm Your Deletion", getContext(), data);

    }

    public Map<String, Object> getDataFromTxtViews() {
        Map<String, Object> ProfileData = new HashMap<>();
        ProfileData.put("Age", binding.studentAge.getText().toString().trim());
        ProfileData.put("name", binding.StudentName.getText().toString().trim());
        ProfileData.put("email", binding.studentMail.getText().toString().trim());
        ProfileData.put("Sex", binding.studentGender.getText().toString().trim());
        ProfileData.put("userID", currentStudent.getValue().getUserId());
        ProfileData.put("phone number", binding.phoneNumber.getText().toString().trim());
        try {
            ProfileData.put("RollNo", Integer.valueOf(binding.StudentRollNo.getText().toString().trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ProfileData.put("membership", binding.feesValidity.getSelectedItem().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ProfileData.put("session", binding.session.getSelectedItem().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProfileData;
    }

    private void AddStudentToAcademy() throws ParseException {
        binding.progress.setVisibility(View.VISIBLE);
        Map<String, Object> ProfileData = new HashMap<>();
        ProfileData.put("Age", binding.studentAge.getText().toString().trim());
        ProfileData.put("name", binding.StudentName.getText().toString().trim());
        ProfileData.put("email", binding.studentMail.getText().toString().trim());
        ProfileData.put("Sex", binding.studentGender.getText().toString().trim());
        ProfileData.put("phone number", binding.phoneNumber.getText().toString().trim());
        ProfileData.put("userID", currentStudent.getValue().getUserId());
        ProfileData.put("session", binding.session.getSelectedItem().toString().trim());
        ProfileData.put("membership", binding.feesValidity.getSelectedItem().toString().trim());
        if (binding.StudentRollNo.getText().toString().equals("null")) {
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
                    Log.e("TAG", "onComplete: error in adding student " + task.getException());
                    Toast.makeText(getContext(), "Some Error Occured " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                binding.progress.setVisibility(View.GONE);
            }
        });
    }


    public void disableAdminEditable() {
        binding.studentMail.setEnabled(false);
        binding.phoneNumber.setEnabled(false);
        binding.session.setEnabled(false);
        binding.StudentRollNo.setEnabled(false);
        binding.feesValidity.setEnabled(false);
    }

    public void enableAll() {
        binding.StudentName.setEnabled(true);
        binding.studentMail.setEnabled(true);
        binding.phoneNumber.setEnabled(true);
        binding.studentMail.setEnabled(true);
        binding.studentGender.setEnabled(true);
        binding.studentAge.setEnabled(true);
        binding.session.setEnabled(true);
        binding.StudentRollNo.setEnabled(true);
        binding.feesValidity.setEnabled(true);
    }

    public void disableAll() {
        binding.StudentName.setEnabled(false);
        binding.studentMail.setEnabled(false);
        binding.phoneNumber.setEnabled(false);
        binding.studentMail.setEnabled(false);
        binding.studentGender.setEnabled(false);
        binding.studentAge.setEnabled(false);
        binding.session.setEnabled(false);
        binding.StudentRollNo.setEnabled(false);
        binding.feesValidity.setEnabled(false);
    }

    @Override
    public void onClick(View v) {//open bottom sheet for contacting user
        Log.e("TAG", "onClick: running");
        ContactOptions.newInstance(currentStudent.getValue().getName(), currentStudent.getValue().getPhone()).show(getChildFragmentManager(), "contactStudent");
    }
}