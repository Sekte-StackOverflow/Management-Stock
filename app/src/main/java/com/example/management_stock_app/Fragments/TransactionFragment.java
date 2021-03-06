package com.example.management_stock_app.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.Models.Transaksi;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.LocalDateTime;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TransactionFragment extends Fragment {
    private final String TAG = "TRANSACTION_FRAGMENT";

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private Button btnSave, btnChoose, btnCancel;
    private ImageButton btnInc, btnDec;
    private EditText code, productName, number;
    private TextView currentStock;
    private RadioGroup radioGroup;

    private AlertDialog.Builder builder;
    private List<Barang> barangs = new ArrayList<>();
    private String[] name;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AndroidThreeTen.init(getContext());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnInc = view.findViewById(R.id.btn_incr);
        btnDec = view.findViewById(R.id.btn_decr);
        btnSave = view.findViewById(R.id.btn_simpan);
        btnCancel = view.findViewById(R.id.btn_cancel_transaction);
        btnChoose = view.findViewById(R.id.choose_product);
        code = view.findViewById(R.id.code_product);
        productName = view.findViewById(R.id.name_product);
        number = view.findViewById(R.id.number);
        currentStock = view.findViewById(R.id.stock_number);
        radioGroup = view.findViewById(R.id.radioGroup);

        number.setText("0");
        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(number.getText().toString());
                number.setText(String.valueOf(n + 1));
            }
        });

        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(number.getText().toString());
                if (n > 0) {
                    number.setText(String.valueOf(n - 1));
                }
            }
        });
        btnChoose.setClickable(false);
        firestore.collection("Users").document(auth.getUid())
                .collection("Barang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot doc :
                                task.getResult()) {
                            barangs.add(new Barang(
                                    doc.getId(),
                                    doc.get("nama").toString(),
                                    doc.get("gambar").toString(),
                                    Integer.valueOf(doc.get("stock").toString()),
                                    Integer.valueOf(doc.get("harga").toString())
                            ));
                        }
                        name = new String[barangs.size()];
                        for (int i = 0; i < name.length; i++) {
                            name[i] = barangs.get(i).getNama();
                        }
                        builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Product List");
                        builder.setItems(name, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Barang barang = barangs.get(which);
                                code.setText(barang.getCode());
                                productName.setText(barang.getNama());
                                currentStock.setText(String.valueOf(barang.getStock()));
                            }
                        });
                        btnChoose.setClickable(true);
                        btnChoose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Cannot Get Data!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = LocalDateTime.now().toString();
                String name = productName.getText().toString();
                String cd = code.getText().toString();
                String changeStock = number.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(selectedId);
                if (!date.equals("") && !name.equals("") && !cd.equals("") && !(changeStock.equals("0") || changeStock.equals("")) && radioButton != null) {
                    String status = radioButton.getText().toString();
                    Transaksi transaksi = new Transaksi(cd, name, date,Integer.valueOf(changeStock), status);
//                    System.out.println("Product Name: "+ transaksi.getName() + ", status =" + transaksi.getStatus()+"["+ transaksi.getCurrentStock() +"], Now Stock= "+ curStock);
                    saveTransaksi(transaksi);
                } else {
                    Toast.makeText(getContext(), "Please Check Your Form!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveTransaksi(final Transaksi transaksi) {
        firestore.collection("Users").document(auth.getUid()).collection("Transaksi")
                .add(transaksi).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    int curStock = Integer.valueOf(currentStock.getText().toString());
                    if (transaksi.getStatus().equals("IN")) {
                        curStock += transaksi.getCurrentStock();
                    } else {
                        curStock -= transaksi.getCurrentStock();
                    }
                    updateBarangStock(transaksi.getId(), curStock);
                    Toast.makeText(getContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(getContext(), "Transaction Failed: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateBarangStock(String code ,int stock) {
        firestore.collection("Users").document(auth.getUid())
                .collection("Barang").document(code).update("stock", stock)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Update Barang[Stock] Success: " + task.isSuccessful());
                        } else {
                            Log.d(TAG, "Failed update Barang[Stock]: " + task.getException().getMessage());
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
