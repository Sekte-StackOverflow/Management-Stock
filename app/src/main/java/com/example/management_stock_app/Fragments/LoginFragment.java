package com.example.management_stock_app.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.management_stock_app.Models.User;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private OnFragmentInteractionListener mListener;
    private EditText emailField, passwordField;
    private Button btnLogin, btnReg;
    private FirebaseAuth fireAuth;
    private FirebaseUser user;
    private FirebaseFirestore mfFirestore;
    private ProgressBar loadingBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        emailField = view.findViewById(R.id.input_username);
        passwordField = view.findViewById(R.id.input_password);
        loadingBar = view.findViewById(R.id.progressBar3);
        btnLogin = view.findViewById(R.id.btn_login);
        btnReg = view.findViewById(R.id.btn_register);
        fireAuth = FirebaseAuth.getInstance();
        mfFirestore = FirebaseFirestore.getInstance();

        loadingBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String pass = passwordField.getText().toString();
                if (!email.equals("") && !pass.equals("")) {
                    signIn(email, pass);
                }
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.registrationPhase();
                }
            }
        });

        return view;
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void buttonGone() {
        btnReg.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
    }
    private void buttonVisible() {
        btnReg.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.GONE);
    }

    private void signIn(final String email, String pass) {
        buttonGone();
        fireAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "LoginUser:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    if (task.getResult().getUser() != null) {
                        user =  task.getResult().getUser();
                        mListener.onLoginSuccess();
                    }
                } else {
                    buttonVisible();
                    Toast.makeText(getActivity(), "Failed Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginSuccess();
        void registrationPhase();
    }
}
