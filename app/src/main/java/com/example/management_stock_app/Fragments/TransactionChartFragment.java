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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.management_stock_app.R;
import com.example.management_stock_app.ViewModels.TransactionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TransactionChartFragment extends Fragment {
    private int hand, in, out;
    private List<PieEntry> data = new ArrayList<>();
    private TransactionViewModel transactionViewModel;

    private OnFragmentInteractionListener mListener;

    public TransactionChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        transactionViewModel = ViewModelProviders.of(requireActivity()).get(TransactionViewModel.class);
        return inflater.inflate(R.layout.fragment_transaction_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PieChart chart = view.findViewById(R.id.stock_pie_chart);
        RelativeLayout rl = view.findViewById(R.id.layout_Chart);
        data.add(new PieEntry(100, "Hand"));
        data.add(new PieEntry(20, "Incoming"));
        data.add(new PieEntry(40, "Outcome"));
        int[] color = new int[]{Color.GRAY, Color.CYAN, Color.MAGENTA};
        PieDataSet pieDataSet = new PieDataSet(data, "");
        pieDataSet.setColors(color);
        PieData pieData = new PieData(pieDataSet);
        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(false);
        chart.setData(pieData);
        chart.invalidate();
        transactionViewModel.getHand().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                hand = integer;
            }
        });
        transactionViewModel.getIn().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                in = integer;
            }
        });
        transactionViewModel.getOut().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                out = integer;
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
