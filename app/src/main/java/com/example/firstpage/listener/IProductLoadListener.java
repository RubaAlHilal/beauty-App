package com.example.firstpage.listener;

import java.util.List;
import com.example.firstpage.model.ProductModel;


public interface IProductLoadListener {
    void onProductLoadSuccess(List<ProductModel> productModelList);
    void onProductLoadFailed(String message);
}
