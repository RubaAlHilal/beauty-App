package com.example.firstpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.firstpage.eventbus.MyUpdateCartEvent;
import com.example.firstpage.listener.ICartLoadListener;
import com.example.firstpage.model.CartModel;
import com.example.firstpage.model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    ImageView detailedImg, back;
    TextView name, desc, price;
    Button addToCart;

    ProductModel product = null;
    ICartLoadListener iCartLoadListener;

    //private FirebaseDatabase firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        Object obj = getIntent().getSerializableExtra("detailed");

        product = (ProductModel) obj;

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        desc = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_cart);
        back = findViewById(R.id.arrow);


        if(product != null){
            Glide.with(getApplicationContext()).load(product.getImage()).into(detailedImg);
            name.setText(product.getName());
            price.setText(product.getPrice());
            desc.setText(product.getdescription());
        }


        back.setOnClickListener(view ->{
            startActivity(new Intent(this, Product.class));
        });

        addToCart.setOnClickListener(v -> addToCart(product, iCartLoadListener));

    }

    private void addToCart(ProductModel product, ICartLoadListener iCartLoadListener) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("cart")
                .child("UNIQUE_USER_ID");

        userCart.child(product.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            CartModel cartModel = snapshot.getValue(CartModel.class);
                            cartModel.setQuantity(cartModel.getQuantity() + 1);
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("quantity", cartModel.getQuantity());
                            updateData.put("totalPrice", cartModel.getQuantity()
                                    * Float.parseFloat(cartModel.getPrice()));

                            userCart.child(product.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        iCartLoadListener.onCartLoadFailed("Add to Cart Success Failed");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        else{
                            CartModel cartModel = new CartModel();
                            cartModel.setName(product.getName());
                            cartModel.setImage(product.getImage());
                            cartModel.setKey(product.getKey());
                            cartModel.setPrice(product.getPrice());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(product.getPrice()));

                            userCart.child(product.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(aVoid -> {
                                        iCartLoadListener.onCartLoadFailed("Add to Cart Success");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));

                        }
                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

}