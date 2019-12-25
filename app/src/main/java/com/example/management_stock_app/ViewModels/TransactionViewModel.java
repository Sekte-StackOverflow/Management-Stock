package com.example.management_stock_app.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class TransactionViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> hand;
    private MutableLiveData<Integer> out;
    private MutableLiveData<Integer> in;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        hand = new MutableLiveData<>();
        out = new MutableLiveData<>();
        in = new MutableLiveData<>();
    }

    public LiveData<Integer> getHand() {
        return hand;
    }

    public void setHand(Integer hand) {
        this.hand.setValue(hand);
    }

    public LiveData<Integer> getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out.setValue(out);
    }

    public LiveData<Integer> getIn() {
        return in;
    }

    public void setIn(Integer in) {
        this.in.setValue(in);
    }
}
