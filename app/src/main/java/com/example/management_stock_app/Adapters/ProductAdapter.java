package com.example.management_stock_app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.R;

import java.util.List;

public class ProductAdapter extends BaseQuickAdapter<Barang, BaseViewHolder> {
    public ProductAdapter(@Nullable List<Barang> data) {
        super(R.layout.item_product, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Barang product) {
        helper.setText(R.id.product_name, product.getNama())
                .setText(R.id.product_code, product.getCode())
                .setText(R.id.product_stock, String.valueOf(product.getStock()))
                .setText(R.id.product_harga, String.valueOf(product.getHarga()));
    }


}
