package com.example.management_stock_app.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

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
 * {@link TransactionChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TransactionChartFragment extends Fragment {
    private final String TAG = "TRANSACTION_CHART";

    private int hand, in, out;
//    private PieChart chart;
//    private List<PieEntry> data;

    private AnyChartView anyChartView;
    private Pie pie;
    private ProgressBar spinner;

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
//        chart = view.findViewById(R.id.stock_pie_chart);
        anyChartView = view.findViewById(R.id.pieChart);
        spinner = view.findViewById(R.id.progressBar4);
        anyChartView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        getBarangStock();
    }

    private void chart() {
        spinner.setVisibility(View.GONE);
        pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener() {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("hand", hand));
        data.add(new ValueDataEntry("in", in));
        data.add(new ValueDataEntry("out", out));

        pie.data(data);
        pie.title("Stock Pie Chart");
        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title().text("Description");
//                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("right")
                .itemsLayout(LegendLayout.VERTICAL_EXPANDABLE)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
        anyChartView.setVisibility(View.VISIBLE);
    }

//    private void updateChart() {
//        data = new ArrayList<>();
//        int[] color = new int[]{Color.GRAY, Color.CYAN, Color.MAGENTA};
//        data.add(new PieEntry(hand, "Hand"));
//        data.add(new PieEntry(in, "Incoming"));
//        data.add(new PieEntry(out, "Outcome"));
//        PieDataSet pieDataSet = new PieDataSet(data, "");
//        pieDataSet.setColors(color);
//        PieData pieData = new PieData(pieDataSet);
//        chart.setDrawEntryLabels(false);
//        chart.setDrawHoleEnabled(false);
//        chart.setData(pieData);
//        chart.invalidate();
//    }

    private void getTransaction() {
        firestore.collection("Users").document(auth.getUid())
                .collection("Transaksi")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
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
                        in = tmpIn;
                        out = tmpOut;
                        chart();
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
                        hand = tmp;
                        getTransaction();
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
