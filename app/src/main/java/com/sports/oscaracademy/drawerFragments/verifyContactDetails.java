package com.sports.oscaracademy.drawerFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.bottomSheet.bottomSheetOtpVerification;
import com.sports.oscaracademy.dialog.dialogs;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class verifyContactDetails extends Fragment {
    dialogs dialog = new dialogs();
    bottomSheetOtpVerification sheet = new bottomSheetOtpVerification();
    EditText phoneNumber;
    private ProgressDialog progressDialog;

    public verifyContactDetails() {
        // Required empty public constructor
    }

    private String mVerificationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.d("TAG", "onVerificationCompleted:" + credential);
            Log.e("TAG", "onVerificationCompleted:  Verification is Completed,");
            Toast.makeText(getContext(), "Hey Completed", Toast.LENGTH_SHORT).show();
            otpReciever(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("TAG", "onVerificationFailed", e);
            dialog.displayDialog(e.getLocalizedMessage(), getContext());
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d("TAG", "onCodeSent:" + verificationId);
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    public static verifyContactDetails newInstance() {
        return new verifyContactDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verify_contact_details, container, false);
        Button button = v.findViewById(R.id.sendOTP);
        progressDialog = new ProgressDialog(getActivity(), R.style.AlertDialog);
        phoneNumber = v.findViewById(R.id.phoneNumber);
        final CharSequence userPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        if (userPhoneNumber != null) {
            if (userPhoneNumber.length() != 0)
                phoneNumber.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3));
        }
        if (userPhoneNumber != null) {
            button.setEnabled(userPhoneNumber == phoneNumber.getText().toString().trim());
        }
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button.setEnabled(s.length() == 10);

                if (userPhoneNumber != null) {
                    if (userPhoneNumber.length() > 9) {
                        if (s.length() == 10)
                            button.setEnabled(!userPhoneNumber.toString().trim().substring(3).contentEquals(s));
                        else
                            button.setEnabled(false);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        button.setOnClickListener(v1 -> {
            sendOTP(phoneNumber.getText().toString());
        });
        return v;
    }

    public void sendOTP(String text) {
        if (text.length() == 10 || text.length() == 11) {

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber("+91 " + text)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(requireActivity())                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
            sheet.show(getChildFragmentManager(), "bottomSheetVerification");
        } else {
            dialogs dialogs = new dialogs();
            dialogs.displayDialog("Check Your Moblile Number", getContext());
        }
    }

    public void otpReciever(String OTP) {
        try {
            dialog.alertDialogLogin(progressDialog, "Verifying");
            Log.e("TAG", "otpReciever: " + OTP);
            Log.e("verification ", "otpReciever: " + mVerificationId);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, OTP);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        updateDB();
                    } else {
                        dialog.dismissDialog(progressDialog);
                        dialog.displayDialog(task.getException().getLocalizedMessage(), getContext());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void otpReciever(PhoneAuthCredential credential) {
        try {
            dialog.alertDialogLogin(progressDialog, "Verifying");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        updateDB();
                    } else {
                        dialog.displayDialog(task.getException().getLocalizedMessage(), getContext());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDB() {
        Map<String, Object> phone = new HashMap<>();
        phone.put("phone number", phoneNumber.getText().toString().trim());
        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid()).set(phone, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.displayDialog(String.format(Locale.getDefault(), "Phone Number %s Sucessfully Added", phoneNumber.getText().toString().trim()), getContext());
                    sheet.dismiss();
                } else {
                    dialog.displayDialog("Phone Number Added To Your Account But Not Able to Notify Academy\n Please Contact Academy To add it to your Profile", getContext());
                    sheet.dismiss();
                }
                dialog.dismissDialog(progressDialog);
            }
        });
        SharedPreferences preferences = getContext().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);

        if (preferences.getString("isStudent", "false").equals("true")) {
            FirebaseFirestore.getInstance().collection("students").document(FirebaseAuth.getInstance().getUid()).set(phone, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialog.displayDialog(String.format(Locale.getDefault(), "Phone Number %s Sucessfully Added", phoneNumber.getText().toString().trim()), getContext());
                        sheet.dismiss();
                    } else {
                        dialog.displayDialog("Phone Number Added To Your Account But Not Able to Notify Academy\n Please Contact Academy To add it to your Profile", getContext());
                        sheet.dismiss();
                    }
                    dialog.dismissDialog(progressDialog);
                }
            });
        }

    }
}