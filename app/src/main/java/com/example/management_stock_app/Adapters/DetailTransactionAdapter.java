package com.example.management_stock_app.Adapters;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.management_stock_app.Models.Transaksi;
import com.example.management_stock_app.R;

import java.util.List;

public class DetailTransactionAdapter extends BaseQuickAdapter<Transaksi, BaseViewHolder> {
    public DetailTransactionAdapter(@Nullable List<Transaksi> data) {
        super(R.layout.item_detail_transaction, data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Transaksi item) {
        TextView sts = helper.getView(R.id.item_trs_status);
        TextView changes = helper.getView(R.id.item_trs_change_stock);
        String[] date = item.getDate().split("T", 2);
        sts.setText(item.getStatus());
        String num = String.valueOf(item.getCurrentStock());
        if (item.getStatus().equals("IN")) {
            sts.setTextColor(ContextCompat.getColor(mContext, R.color.myGreen));
            changes.setTextColor(ContextCompat.getColor(mContext, R.color.myGreen));
            changes.setText("+" + num);
        } else {
            sts.setTextColor(ContextCompat.getColor(mContext, R.color.myRed));
            changes.setTextColor(ContextCompat.getColor(mContext, R.color.myRed));
            changes.setText("-" + num);
        }
        helper.setText(R.id.item_trs_name, item.getName())
                .setText(R.id.item_trs_code, item.getId())
                .setText(R.id.item_date, date[0]);
        helper.addOnClickListener(R.id.cardView2);
    }
}
