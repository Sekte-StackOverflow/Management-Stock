package com.example.management_stock_app.Adapters;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseQuickAdapter<Barang, BaseViewHolder> {
    public ProductAdapter(@Nullable List<Barang> data) {
        super(R.layout.item_single_product, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Barang product) {
        ImageView img = helper.getView(R.id.image_product);
        String uri = "https://static.thenounproject.com/png/583402-200.png";
        if (product.getGambar().equals("kosong")) {
            Picasso.get().load(uri).into(img);
        } else {
            Picasso.get().load(product.getGambar()).into(img);
        }
        helper.setText(R.id.text_name, product.getNama())
                .setText(R.id.text_code, product.getCode())
//                .setText(R.id.product_stock, String.valueOf(product.getStock()))
                .setText(R.id.text_price, String.valueOf(product.getHarga()));
    }
}
