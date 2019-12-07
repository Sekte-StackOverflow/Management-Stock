package com.example.management_stock_app.Fragments;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.management_stock_app.Adapters.ProductAdapter;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.Models.User;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {
    private final String TAG = "ProductsFragment";
    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

    public List<Barang> barangList = new ArrayList<>();
    private User userData;
    private ProductAdapter adapter;
    private  String userEmail;

    private TextView testView;
    private Button btnTest;
    private RecyclerView productsView;
    private ProgressBar spinner;


    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = firebaseUser.getEmail();
        btnTest = view.findViewById(R.id.btn_test);
        testView = view.findViewById(R.id.textView2);
        productsView = view.findViewById(R.id.rv_products);
        spinner = view.findViewById(R.id.progressProduct);
        spinner.setVisibility(View.GONE);
        productsView.setVisibility(View.GONE);
        databaseCheck(userEmail);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addNewProduct();
            }
        });

        return view;
    }
    public void databaseCheck(String email) {
        productsView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        firestore.collection("Users").whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot user: task.getResult()) {
                                userData = new User(user.getId(), user.get("name").toString(), user.get("email").toString());
                                testView.setText("User Account: "+userData.getName());
                                try {
                                    JSONArray jsonArray = new JSONArray(user.get("Barang").toString());
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String jString = jsonArray.get(i).toString();
                                            JsonParser parser = new JsonParser();
                                            JsonObject object = parser.parse(jString).getAsJsonObject();
                                            Barang barang = new Barang(
                                                    object.get("code").getAsString(),
                                                    object.get("nama").getAsString(),
                                                    object.get("stock").getAsInt(),
                                                    object.get("gambar").toString(),
                                                    object.get("harga").getAsInt());
                                            barangList.add(barang);
                                        }
                                        mListener.setDataBarang(barangList, userData);
                                        adapterData(barangList);
                                    } else {
                                        Toast.makeText(getContext(), "Your Have no Product Yet", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Cannot get data", Toast.LENGTH_SHORT).show();
                                }
                                spinner.setVisibility(View.GONE);
                                productsView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Failed load data Barang", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addBarangDatabase(String id, final Barang data) {
        if (data != null) {
            barangList.add(data);
        }
        firestore.collection("Users").document(id)
                .update("Barang", barangList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (data != null) {
                        adapter.addData(data);
                    }
                    Toast.makeText(getContext(), "Add Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void adapterData(List<Barang> list) {
        adapter = new ProductAdapter(list);
        ItemDragAndSwipeCallback swipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeCallback);
        touchHelper.attachToRecyclerView(productsView);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) { }
            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {
                Toast.makeText(getContext(), "Clear View", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) { }
            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
                Toast.makeText(getContext(), "Swipe Moving =" + b, Toast.LENGTH_SHORT).show();
            }
        });
        productsView.setAdapter(adapter);
        productsView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void buttonProduct();
        void addNewProduct();
        void setDataBarang(List<Barang> dataBarang, User user);
    }

}
