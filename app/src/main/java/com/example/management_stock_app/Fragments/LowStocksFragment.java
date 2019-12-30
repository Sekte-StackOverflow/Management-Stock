package com.example.management_stock_app.Fragments;

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
import com.example.management_stock_app.Adapters.LowStockAdapter;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LowStocksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LowStocksFragment extends Fragment {
    private final String TAG = "LOW_STOCK_FRAGMENT";

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private RecyclerView rvLowStocks;
    private ProgressBar spinner;

    private List<Barang> lowStockList = new ArrayList<>();
    private LowStockAdapter adapter;
    private int stockMinimum = 25;

    public LowStocksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_low_stocks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvLowStocks = view.findViewById(R.id.rv_lower_stock);
        spinner = view.findViewById(R.id.stock_loading);

        rvLowStocks.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        getLowerStock();
    }

    public void getLowerStock() {
        spinner.setVisibility(View.VISIBLE);
        firestore.collection("Users").document(auth.getUid())
                .collection("Barang").whereLessThanOrEqualTo("stock", stockMinimum)
                .orderBy("stock", Query.Direction.ASCENDING).limit(10)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot doc :
                                task.getResult()) {
                            Barang barang = new Barang(
                                    doc.getId(),
                                    doc.get("nama").toString(),
                                    doc.get("gambar").toString(),
                                    Integer.valueOf(doc.get("stock").toString()),
                                    Integer.valueOf(doc.get("harga").toString())
                            );
                            lowStockList.add(barang);
                        }
                        spinner.setVisibility(View.GONE);
                        showList();
                    } else {
                        spinner.setVisibility(View.GONE);
                        Log.d(TAG, "Empty DataStore!");
                    }
                } else {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed Get Data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showList() {
        adapter = new LowStockAdapter(lowStockList);
        adapter.openLoadAnimation();
        adapter.isFirstOnly(true);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Barang barang = lowStockList.get(position);
                Toast.makeText(getContext(), "Name: " + barang.getNama(), Toast.LENGTH_SHORT).show();
            }
        });
        rvLowStocks.setAdapter(adapter);
        rvLowStocks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLowStocks.setVisibility(View.VISIBLE);
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
