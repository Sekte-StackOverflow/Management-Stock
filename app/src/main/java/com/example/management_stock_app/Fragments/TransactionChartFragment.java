package com.example.management_stock_app.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.Models.Transaksi;
import com.example.management_stock_app.R;
import com.example.management_stock_app.ViewModels.TransactionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TransactionChartFragment extends Fragment {
    private final String TAG = "TRANSACTION_CHART";

    private int hand, in, out;
    private PieChart chart;
    private List<PieEntry> data;
    private TransactionViewModel transactionViewModel;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private OnFragmentInteractionListener mListener;

    public TransactionChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        transactionViewModel = ViewModelProviders.of(requireActivity()).get(TransactionViewModel.class);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        hand = 0;
        out = 0;
        in = 0;
        return inflater.inflate(R.layout.fragment_transaction_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart = view.findViewById(R.id.stock_pie_chart);
        getTransaction();
        getBarangStock();
        updateChart();
        transactionViewModel.getHand().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                hand = integer;
                updateChart();
            }
        });
        transactionViewModel.getIn().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                in = integer;
                updateChart();
            }
        });
        transactionViewModel.getOut().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                out = integer;
                updateChart();
            }
        });
    }

    private void updateChart() {
        data = new ArrayList<>();
        int[] color = new int[]{Color.GRAY, Color.CYAN, Color.MAGENTA};
        data.add(new PieEntry(hand, "Hand"));
        data.add(new PieEntry(in, "Incoming"));
        data.add(new PieEntry(out, "Outcome"));
        PieDataSet pieDataSet = new PieDataSet(data, "");
        pieDataSet.setColors(color);
        PieData pieData = new PieData(pieDataSet);
        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(false);
        chart.setData(pieData);
        chart.invalidate();
    }

    private void getTransaction() {
        firestore.collection("Users").document(auth.getUid())
                .collection("Transaksi")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        int tmpIn =0, tmpOut=0;
                        for (DocumentSnapshot doc : task.getResult()) {
                            String stat = doc.get("status").toString();
                            if (stat.equals("IN")) {
                                int stk = Integer.valueOf(doc.get("currentStock").toString());
                                tmpIn += stk;
                            } else {
                                int stk = Integer.valueOf(doc.get("currentStock").toString());
                                tmpOut += stk;
                            }
                        }
                        transactionViewModel.setIn(tmpIn);
                        transactionViewModel.setOut(tmpOut);
                    } else {
                        Log.d(TAG, "Empty data");
                    }
                } else {
                    Log.d(TAG, "Connection Failed");
                }
            }
        });
    }


    private void getBarangStock() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firestore.collection("Users").document(auth.getUid())
                .collection("Barang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        int tmp = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            int stock = Integer.valueOf(document.get("stock").toString());
                             tmp += stock;
                        }
                        transactionViewModel.setHand(tmp);
                    } else {
                        Toast.makeText(getContext(), "Data is Empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String e = task.getException().getMessage();
                    Log.d(TAG, e);
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event

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
