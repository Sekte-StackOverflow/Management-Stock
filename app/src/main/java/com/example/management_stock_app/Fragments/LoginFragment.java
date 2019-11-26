package com.example.management_stock_app.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.management_stock_app.Models.Admin;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

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

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailField = view.findViewById(R.id.input_username);
        passwordField = view.findViewById(R.id.input_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnReg = view.findViewById(R.id.btn_register);
        fireAuth = FirebaseAuth.getInstance();
        user = fireAuth.getCurrentUser();
        mfFirestore = FirebaseFirestore.getInstance();

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
                String email = emailField.getText().toString();
                String pass = passwordField.getText().toString();
                if (!email.equals("") && !pass.equals("")) {
                    createNewUser(email, pass);
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

    private void createNewUser(String email, String pass) {
        fireAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Success Register", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed Register", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(final String email, String pass) {
        fireAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "LoginUser:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    user = task.getResult().getUser();
                    String username = usernameFromEmail(user.getEmail());
                    Toast.makeText(getActivity(), "Success as:" + username, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
