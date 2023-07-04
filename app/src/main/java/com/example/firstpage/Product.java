package com.example.firstpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.firstpage.adapter.MyProductAdapter;
import com.example.firstpage.eventbus.MyUpdateCartEvent;
import com.example.firstpage.listener.ICartLoadListener;
import com.example.firstpage.listener.IProductLoadListener;
import com.example.firstpage.model.CartModel;
import com.example.firstpage.model.ProductModel;
import com.example.firstpage.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Product extends AppCompatActivity implements IProductLoadListener, ICartLoadListener{

    @BindView(R.id.recycler_drink)
    RecyclerView recyclerDrink;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;
    @BindView(R.id.arrowBack)
    ImageView arrowBack;
    @BindView(R.id.badge)
    NotificationBadge badge;

    IProductLoadListener productLoadListener;
    ICartLoadListener cartLoadListener;
    TextView mTextview;


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event){
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        init();
        loadProductfromFirebase();
        countCartItem();
        mTextview = (TextView)findViewById(R.id.txtPageTitle);
        mTextview.setText(getIntent().getStringExtra("category"));
        arrowBack.setOnClickListener(view -> {startActivity(new Intent(this, Discover.class));});
    }

    private void loadProductfromFirebase() {
        List<ProductModel> productModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference(getIntent().getStringExtra("category"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot hairSnapshot:snapshot.getChildren()){
                                ProductModel productModel = hairSnapshot.getValue(ProductModel.class);
                                productModel.setKey(hairSnapshot.getKey());
                                productModels.add(productModel);
                            }
                            productLoadListener.onProductLoadSuccess(productModels);
                        }
                        else{
                            productLoadListener.onProductLoadFailed("Can't Find Skincare Products");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        productLoadListener.onProductLoadFailed(error.getMessage());
                    }
                });
    }

    private void init(){
        ButterKnife.bind(this);
        productLoadListener = Product.this;
        cartLoadListener = Product.this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Product.this, 2);
        recyclerDrink.setLayoutManager(gridLayoutManager);
        recyclerDrink.addItemDecoration(new SpaceItemDecoration());

        btnCart.setOnClickListener(v -> startActivity (new Intent(Product.this,CartActivity.class)));
    }

    @Override
    public void onProductLoadSuccess(List<ProductModel> productModelList) {
        MyProductAdapter adapter = new MyProductAdapter(Product.this, productModelList, cartLoadListener);
        recyclerDrink.setAdapter(adapter);
    }

    @Override
    public void onProductLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum = 0;
        for (CartModel cartModel: cartModelList)
            cartSum += cartModel.getQuantity();
        badge.setNumber(cartSum);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartSnapshot:snapshot.getChildren()){
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}