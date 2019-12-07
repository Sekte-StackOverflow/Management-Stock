package com.example.management_stock_app.Adapters;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseItemDraggableAdapter<Barang, BaseViewHolder> {
    public ProductAdapter(@Nullable List<Barang> data) {
        super(R.layout.item_product, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Barang product) {
        ImageView img = helper.getView(R.id.product_image);
        String uri = "https://static.thenounproject.com/png/583402-200.png";
        helper.setText(R.id.product_name, product.getNama())
                .setText(R.id.product_code, product.getCode())
                .setText(R.id.product_stock, String.valueOf(product.getStock()))
                .setText(R.id.product_harga, String.valueOf(product.getHarga()));
        Picasso.get().load(uri).into(img);
    }
}
