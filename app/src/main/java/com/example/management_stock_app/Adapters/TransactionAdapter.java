package com.example.management_stock_app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.management_stock_app.Models.Transaksi;

import java.util.List;

public class TransactionAdapter extends BaseQuickAdapter<Transaksi, BaseViewHolder> {
    public TransactionAdapter(@Nullable List<Transaksi> data) {
        super(data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Transaksi item) {

    }
}
