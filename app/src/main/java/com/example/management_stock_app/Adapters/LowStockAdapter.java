package com.example.management_stock_app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.R;

import java.util.List;

public class LowStockAdapter extends BaseQuickAdapter<Barang, BaseViewHolder> {
    public LowStockAdapter(@Nullable List<Barang> data) {
        super(R.layout.item_stock, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Barang item) {
        helper.setText(R.id.stock_name, item.getNama())
                .setText(R.id.stock_code, item.getCode())
                .setText(R.id.stock_number_item, String.valueOf(item.getStock()));
    }
}
