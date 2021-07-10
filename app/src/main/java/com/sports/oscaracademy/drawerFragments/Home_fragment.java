package com.sports.oscaracademy.drawerFragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.dashBoard_adapter;
import com.sports.oscaracademy.data.DashBoardData;
import com.sports.oscaracademy.databinding.FragmentHomeFragmentBinding;
import com.sports.oscaracademy.viewModel.HomeModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Home_fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private HomeModel model;
    private RecyclerView rcv;
    private dashBoard_adapter adapter;
    private FirebaseFirestore db;
    private ArrayList<DashBoardData> datalist = new ArrayList<>();
    private ProgressBar progressBar;
    private FragmentHomeFragmentBinding binding;

    public Home_fragment() {
    }
    public static Home_fragment newInstance() {
        Home_fragment fragment = new Home_fragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white,getContext().getTheme()));
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_fragment,null,false);
        progressBar = binding.progress;
        rcv = binding.MainRCV;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);
        getItem();
        return binding.getRoot();
    }
    public void getItem() {
        final String[] collection = {"Student_dashboard"};
        progressBar.setVisibility(View.VISIBLE);
        db.collection("userType_private").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("TAG", "onSuccess: document snapshot" + documentSnapshot);
                        Log.d("TAG", "onSuccess: document role" + documentSnapshot.get("role"));
                        collection[0] = String.valueOf(documentSnapshot.get("role"));
                        db.collection(collection[0]).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        ArrayList<String> arr = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("section");
                                        ArrayList<String> imgURL = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("sectionImg");
                                        Log.d("TAG", "onSuccess: " + arr);
                                        Log.d("TAG", "onSuccess: " + imgURL);
                                        int i = 0;
                                        for (String s : arr) {
                                            Log.e("TAG", "onSuccess: field " + i + s);
                                            datalist.add(new DashBoardData(imgURL.get(i), s));
                                            i++;
                                        }
                                        adapter = new dashBoard_adapter(datalist); // need data over here from firebase server
                                        Log.d("TAG", "onCreate: " + datalist.get(1).getFieldname());
                                        rcv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                        rcv.setAdapter(adapter);
                                        i = 0;
                                        progressBar.setVisibility(View.GONE);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "invalid user", Toast.LENGTH_SHORT).show();
            }
        });



    }
}