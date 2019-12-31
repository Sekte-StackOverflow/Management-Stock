package com.example.management_stock_app.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailProductFragment extends Fragment {
    private final int PICK_IMAGE_REQUEST = 71;
    private final String TAG = "DETAIL_PRODUCT_FRAGMENT";

    private OnFragmentInteractionListener mListener;

    private EditText code, name, price, stock;
    private ImageView image;
    private Button change, save, delete;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Barang barang;
    private Uri filepath;
    private Bitmap bitmap;

    private ProgressDialog progressDialog;

    public DetailProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressDialog = new ProgressDialog(getContext());
        return inflater.inflate(R.layout.fragment_detail_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        code = view.findViewById(R.id.dt_code);
        name = view.findViewById(R.id.dt_name);
        price = view.findViewById(R.id.dt_price);
        stock = view.findViewById(R.id.dt_stock);
        image = view.findViewById(R.id.dt_image);

        save = view.findViewById(R.id.dt_btn_save);
        delete = view.findViewById(R.id.dt_btn_delete);
        change = view.findViewById(R.id.dt_btn_change);

        code.setEnabled(false);
        stock.setEnabled(false);

        if (barang != null) {
            code.setText(barang.getCode());
            name.setText(barang.getNama());
            price.setText(String.valueOf(barang.getHarga()));
            stock.setText(String.valueOf(barang.getStock()));
            Picasso.get().load(barang.getGambar()).into(image);
        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        image.setClickable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nm = name.getText().toString();
                String pr = price.getText().toString();
                if (!nm.equals("") && !pr.equals("")) {
                    if (Integer.parseInt(pr) > 0) {
                        barang.setNama(nm);
                        barang.setHarga(Integer.valueOf(pr));
                        //delete first
                        deleteImage(barang.getGambar());
                        // then upload
                        uploadImage();
                    } else {
                        Toast.makeText(getContext(), "Price must greater than 0 (zero)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Check your form!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barang != null) {
                    deleteDialog();
                }
            }
        });

    }

    public void setInformation(Barang barang) {
        this.barang = barang;
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void deleteDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Warning !");
        alert.setMessage("Are you sure want to delete this product, note: delete product couldn't the transaction history!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteImage(barang.getGambar());
                        deleteData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void deleteData() {
        firestore.collection("Users").document(auth.getUid())
                .collection("Barang").document(barang.getCode())
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Delete Success full!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteImage(String imageUrl) {
        StorageReference sr = storage.getReferenceFromUrl(imageUrl);
        sr.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Success get reference");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }

    private void uploadImage() {
        progressDialog.setTitle("Update...");
        progressDialog.show();
        final StorageReference file = storageReference.child("Products/").child(auth.getUid() + "/").child(UUID.randomUUID().toString());
        file.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload complete "+taskSnapshot.getMetadata().getPath());
                file.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri url = task.getResult();
                            updateData(url);
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("uploaded " + (int) progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void updateData(Uri uriImage) {
        String urlString = uriImage.toString();
        HashMap<String, Object> data = new HashMap<>();
        data.put("nama", barang.getNama());
        data.put("gambar", urlString);
        data.put("stock", barang.getStock());
        data.put("harga", barang.getHarga());
        firestore.collection("Users").document(auth.getUid())
                .collection("Barang").document(barang.getCode())
                .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Data Changes Saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to save data!", Toast.LENGTH_SHORT).show();
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
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
