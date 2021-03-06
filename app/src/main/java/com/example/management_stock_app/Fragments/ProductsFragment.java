package com.example.management_stock_app.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.management_stock_app.Adapters.ProductAdapter;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.Models.User;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {
    private final String TAG = "ProductsFragment";
    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;

    private List<Barang> barangList;
    private User userData;
    private ProductAdapter adapter;
    private String userEmail;

    private TextView testView;
    private RecyclerView productsView;
    private ProgressBar spinner;
    private FloatingActionButton btnAdd;
    private SearchView searchView;


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
        storage = FirebaseStorage.getInstance();
        userEmail = firebaseUser.getEmail();
        barangList = new ArrayList<>();

        searchView = view.findViewById(R.id.search_box);
        productsView = view.findViewById(R.id.rv_products);
        spinner = view.findViewById(R.id.progressProduct);
        btnAdd = view.findViewById(R.id.add_new_product);
        spinner.setVisibility(View.GONE);
        productsView.setVisibility(View.GONE);

        getDatabase();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Barang> tmp = new ArrayList<>();
                tmp.clear();
                String text = newText.toLowerCase(Locale.getDefault());
                if (text.length() == 0) {
                    adapterData(barangList);
                } else {
                    for (Barang brg :
                            barangList) {
                        if (brg.getNama().toLowerCase(Locale.getDefault()).contains(text)) {
                            tmp.add(brg);
                        }
                    }
                    adapterData(tmp);
                }
                return false;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.gotoProductIn();
                }
            }
        });

        return view;
    }

    private void getDatabase() {
        spinner.setVisibility(View.VISIBLE);
        firestore.collection("Users").document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot myData = task.getResult();
                    userData = new User(myData.getId(), myData.get("name").toString(), myData.get("email").toString());
                    getSubCollection(userData);
                } else {
                    Toast.makeText(getContext(), "Cannot get Collection", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getSubCollection(User user) {
        firestore.collection("Users").document(user.getId())
                .collection("Barang").orderBy("nama", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot product: task.getResult()) {
                            Barang barang = new Barang(product.getId(),
                                    product.get("nama").toString(),
                                    product.get("gambar").toString(),
                                    Integer.valueOf(product.get("stock").toString()),
                                    Integer.valueOf(product.get("harga").toString()));
                            barangList.add(barang);
                            spinner.setVisibility(View.GONE);
                            productsView.setVisibility(View.VISIBLE);
                            adapterData(barangList);
                        }
                    } else {
                        spinner.setVisibility(View.GONE);
                        Log.d(TAG, "Your Data Barang Is Empty");
                    }

                } else {
                    spinner.setVisibility(View.GONE);
                    Log.d(TAG, "Failed Please Check Your Connection");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }

    private void deleteProduct(String code) {
        firestore.collection("Users").document(firebaseUser.getUid())
                .collection("Barang").document(code).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Delete Success!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Success Delete Barang Item");
                        } else {
                            Toast.makeText(getContext(), "Delete Failed!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void adapterData(List<Barang> list) {
        adapter = new ProductAdapter(list);
        adapter.openLoadAnimation();
        adapter.isFirstOnly(true);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mListener != null) {
                    Barang selected = barangList.get(position);
                    mListener.detailBarang(selected);
                }
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
        void addNewProduct(User user);
        void setDataBarang(List<Barang> dataBarang, User user);
        void detailBarang(Barang barang);
        void gotoProductIn();
    }

}
