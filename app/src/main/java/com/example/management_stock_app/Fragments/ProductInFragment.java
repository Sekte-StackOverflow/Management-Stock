package com.example.management_stock_app.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.management_stock_app.MainActivity;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.Models.User;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProductInFragment extends Fragment {
    private final int PICK_IMAGE_REQUEST = 71;
    private final String TAG = "PRODUCT_IN_FRAGMENT";

    private OnFragmentInteractionListener mListener;
    private EditText name, stock, price, code;
    private ImageView display;
    private Button btnSave, btnChoose;
    private ProgressDialog progressDialog;

    private Uri filepath;
    private Bitmap bitmap;

    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    public ProductInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_in, container, false);
        name = view.findViewById(R.id.new_product_name);
        stock = view.findViewById(R.id.new_product_stock);
        price = view.findViewById(R.id.new_product_price);
        code = view.findViewById(R.id.new_product_code);
        display = view.findViewById(R.id.product_display);
        btnChoose = view.findViewById(R.id.btn_choose_pic);
        btnSave = view.findViewById(R.id.btn_save_product);
        progressDialog = new ProgressDialog(getContext());

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNewBarang();
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadNewBarang() {
        final String nm = name.getText().toString();
        final String cd = code.getText().toString();
        String strStock = stock.getText().toString();
        String strPrice = price.getText().toString();
        if (!nm.equals("") && !cd.equals("") && !strPrice.equals("") && !strStock.equals("")) {
            final int stk = Integer.valueOf(strStock);
            final int hrg = Integer.valueOf(strPrice);
            if (filepath != null) {
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                final StorageReference file = storageReference.child("Products/").child(user.getUid() + "/").child(UUID.randomUUID().toString());
                file.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "Upload Success!", Toast.LENGTH_SHORT).show();
                                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Barang barang = new Barang(cd, nm, uri.toString(), stk, hrg);
                                        insertBarang(barang);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Upload Failed!!:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("uploaded " + (int) progress + "%");
                            }
                        });
            }
        } else {
            Toast.makeText(getContext(), "Please fill The blank text field!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertBarang(Barang barang) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("nama", barang.getNama());
        data.put("gambar", barang.getGambar());
        data.put("stock", barang.getStock());
        data.put("harga", barang.getHarga());
        firestore.collection("Users").document(user.getUid())
                .collection("Barang").document(barang.getCode())
                .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Complete insert data barang");
                    progressDialog.dismiss();
                } else {
                    Log.d(TAG, "InsertData Failed");
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                display.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        void goToProductList();
        void newBarangAdded(Barang barang);
    }
}
