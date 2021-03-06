package com.example.management_stock_app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.management_stock_app.Actitvity.LoginActivity;
import com.example.management_stock_app.Models.User;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText email, password;
    private Button btnReg;
    private ProgressBar spinner;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        email = view.findViewById(R.id.reg_email);
        password = view.findViewById(R.id.reg_password);
        btnReg = view.findViewById(R.id.btn_new_account);
        spinner = view.findViewById(R.id.progress_register);
        spinner.setVisibility(View.GONE);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();
                createNewUser(userEmail, pass);

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    private void createNewUser(String newEmail, String pass) {
        spinner.setVisibility(View.VISIBLE);
        final String email = newEmail;
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String code = task.getResult().getUser().getUid();
                    writeNewUser(code ,email);
                } else {
                    Toast.makeText(getActivity(), "Failed Register", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeNewUser(String code ,String email) {
        String username = usernameFromEmail(email);
        User user = new User(username, email);
        firestore.collection("Users").document(code).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Success create new user", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(),
                                    LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Failed create new user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        void tourToLogin();
    }
}
