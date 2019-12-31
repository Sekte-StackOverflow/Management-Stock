package com.example.management_stock_app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.management_stock_app.Models.Transaksi;
import com.example.management_stock_app.R;

import java.util.List;

public class TransactionAdapter extends BaseQuickAdapter<Transaksi, BaseViewHolder> {
    public TransactionAdapter(@Nullable List<Transaksi> data) {
        super(R.layout.item_transaction ,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Transaksi item) {
        String date = item.getDate();
        String[] dateOnly = date.split("T", 2);
        helper.setText(R.id.trs_product_name, item.getName())
                .setText(R.id.trs_date, dateOnly[0])
                .setText(R.id.in_or_out, item.getStatus());
    }
}
