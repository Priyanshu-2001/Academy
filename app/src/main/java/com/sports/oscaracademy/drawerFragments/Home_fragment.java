package com.sports.oscaracademy.drawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.dashBoard_adapter;
import com.sports.oscaracademy.data.DashBoardData;
import com.sports.oscaracademy.databinding.FragmentHomeFragmentBinding;

import java.util.ArrayList;

public class Home_fragment extends Fragment {
    private FirebaseAuth mAuth;
    private RecyclerView rcv;
    private dashBoard_adapter adapter;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private FragmentHomeFragmentBinding binding;

    public Home_fragment() {
    }

    public static Home_fragment newInstance() {
        Home_fragment fragment = new Home_fragment();
        return fragment;
    }

    String collection = "Student_dashboard";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_fragment, null, false);
        progressBar = binding.progress;
        rcv = binding.MainRCV;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);
        getItem();
        return binding.getRoot();
    }

    public static void getUserType(Context context) {
        Log.e("TAG", "getUserType: " + "called");
        String currentuserID = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        SharedPreferences.Editor pref = context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
        store.collection("chatResponders")
                .document("coaches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> coachList = (ArrayList<String>) task.getResult().get("coachesList");
                        if (coachList.contains(currentuserID)) {
                            pref.putString("userType", "-2");
                            pref.apply();
                            Toast.makeText(context, "welcome back Coach home", Toast.LENGTH_SHORT).show();
                        } else {
                            store.collection("chatResponders")
                                    .document("Admin")
                                    .get()
                                    .addOnCompleteListener(it ->
                                            {
                                                if (it.isSuccessful()) {
                                                    ArrayList<String> adminList = (ArrayList<String>) it.getResult().get("adminID");
                                                    Log.e("TAG", "getUserType adminList " + adminList);
                                                    Log.e("TAG", "getUserType adminList " + currentuserID);
                                                    if (adminList.contains(currentuserID)) {
                                                        pref.putString("userType", "-2");
                                                        pref.apply();
                                                        Toast.makeText(context, "welcome back Admin home", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        pref.putString("userType", "1");
                                                        pref.apply();
                                                        Toast.makeText(context, "welcome back Student home", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                        }
                        pref.apply();
                    }
                });
    }

    public void getItem() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("userType_private")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    collection = String.valueOf(documentSnapshot.get("role"));
                    db.collection(collection)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                ArrayList<DashBoardData> datalist = new ArrayList<>();
                                ArrayList<String> arr = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("section");
                                ArrayList<String> imgURL = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("sectionImg");
                                int i = 0;
                                for (String s : arr) {
                                    datalist.add(new DashBoardData(imgURL.get(i), s));
                                    i++;
                                }
                                adapter = new dashBoard_adapter(datalist, getActivity()); // need data over here from firebase server
                                rcv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                rcv.setAdapter(adapter);
                                i = 0;
                                progressBar.setVisibility(View.GONE);

                            })
                            .addOnFailureListener(e -> progressBar.setVisibility(View.GONE));
                }).addOnFailureListener(e -> Toast.makeText(getContext(), "invalid user", Toast.LENGTH_SHORT).show());
    }
}