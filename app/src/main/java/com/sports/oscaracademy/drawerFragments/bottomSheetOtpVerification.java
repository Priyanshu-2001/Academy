package com.sports.oscaracademy.drawerFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sports.oscaracademy.R;

public class bottomSheetOtpVerification extends BottomSheetDialogFragment {
    sendOTP mListener;

    public bottomSheetOtpVerification() {
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.otp_verification_bottom_sheet, container);
        EditText otp = v.findViewById(R.id.OTP);
        Button verifyBtn = v.findViewById(R.id.verifyOTP);
        verifyBtn.setOnClickListener(v1 -> {
//            closeKeyboard();
            hideKeyboardFrom(requireContext(), v);
            mListener.getOTP(otp.getText().toString());
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View touchOutsideView = getDialog().getWindow()
                .getDecorView()
                .findViewById(R.id.touch_outside);
        touchOutsideView.setOnClickListener(null);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (sendOTP) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface sendOTP {
        void getOTP(String OTP);
    }
}
