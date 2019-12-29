package com.example.management_stock_app.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.management_stock_app.Adapters.TransactionAdapter;
import com.example.management_stock_app.Models.Transaksi;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TransactionViewFragment extends Fragment {
    private final String TAG = "TRANSACTION_VIEW_FRAGMENT";

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private RecyclerView viewTransaksi;
    private TextView noDataView;
    private ProgressBar progressBar;

    private List<Transaksi> list = new ArrayList<>();
    private TransactionAdapter adapter;

    public TransactionViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_transaction_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewTransaksi = view.findViewById(R.id.rv_transaction);
        noDataView = view.findViewById(R.id.text_empty_transaction);
        progressBar = view.findViewById(R.id.transaction_progress);

        viewTransaksi.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("Users").document(auth.getUid())
                .collection("Transaksi").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            list.add(new Transaksi(
                                    doc.getId(),
                                    doc.get("name").toString(),
                                    doc.get("tanggal").toString(),
                                    Integer.valueOf(doc.get("stock").toString()),
                                    doc.get("Status").toString()
                            ));
                            adapter = new TransactionAdapter(list);
                            viewTransaksi.setAdapter(adapter);
                            viewTransaksi.setLayoutManager(new LinearLayoutManager(getContext()));
                            progressBar.setVisibility(View.GONE);
                            viewTransaksi.setVisibility(View.VISIBLE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        noDataView.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "No Transaction", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
