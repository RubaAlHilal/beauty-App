package com.example.firstpage.listener;

import com.example.firstpage.model.CartModel;
import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}

