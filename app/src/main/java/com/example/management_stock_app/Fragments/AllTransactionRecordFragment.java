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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.management_stock_app.Adapters.DetailTransactionAdapter;
import com.example.management_stock_app.Models.Transaksi;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllTransactionRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AllTransactionRecordFragment extends Fragment {
    private final String TAG = "ALL_TRANSACTION_FRAGMENT";

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private DetailTransactionAdapter adapter;
    private List<Transaksi> transaksis;

    private RecyclerView recyclerView;
    private ProgressBar spinner;

    public AllTransactionRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        transaksis = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_all_tracsaction_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_all_trsction);
        spinner = view.findViewById(R.id.spinner2);

        recyclerView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        firestore.collection("Users").document(auth.getUid())
                .collection("Transaksi").orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Transaksi transaksi = new Transaksi(
                                    doc.get("id").toString(),
                                    doc.get("name").toString(),
                                    doc.get("date").toString(),
                                    Integer.parseInt(doc.get("currentStock").toString()),
                                    doc.get("status").toString()
                            );
                            transaksis.add(transaksi);
                        }
                        adapter = new DetailTransactionAdapter(transaksis);
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                Toast.makeText(getContext(), "Create On: " + transaksis.get(position).getDate(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        spinner.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    } else {
                        spinner.setVisibility(View.GONE);
                        Log.d(TAG, "Empty Data Transaction");
                    }
                } else {
                    spinner.setVisibility(View.GONE);
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
