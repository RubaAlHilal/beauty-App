package com.example.firstpage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.firstpage.DetailsActivity;
import com.example.firstpage.R;
import com.example.firstpage.eventbus.MyUpdateCartEvent;
import com.example.firstpage.listener.ICartLoadListener;
import com.example.firstpage.listener.IRecyclerViewClickListener;
import com.example.firstpage.model.CartModel;
import com.example.firstpage.model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder>{

    private Context context;
    private List<ProductModel> productModelList;
    private ICartLoadListener iCartLoadListener;

    public MyProductAdapter(Context context, List<ProductModel> productModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.productModelList = productModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProductViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        Glide.with(context)
                .load(productModelList.get(position).getImage())
                .into(holder.imageView);

        holder.txtPrice.setText(new StringBuilder("SR").append(productModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(productModelList.get(position).getName()));

//            holder.setListener((view, adapterPosition) -> {
//                addToCart (drinkModelList.get(position));
//            });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("detailed", productModelList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    private void addToCart(ProductModel productModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("cart")
                .child("UNIQUE_USER_ID");

        userCart.child(productModel.getKey())
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

                            userCart.child(productModel.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        iCartLoadListener.onCartLoadFailed("Add to Cart Success");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        else{
                            CartModel cartModel = new CartModel();
                            cartModel.setName(productModel.getName());
                            cartModel.setImage(productModel.getImage());
                            cartModel.setKey(productModel.getKey());
                            cartModel.setPrice(productModel.getPrice());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(productModel.getPrice()));

                            userCart.child(productModel.getKey())
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


    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView txtName;
        TextView txtPrice;
        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imageView = itemView.findViewById(R.id.imageView);
            unbinder = ButterKnife.bind(MyProductAdapter.this, itemView);

            itemView.setOnClickListener(v ->{
                listener.onRecyclerClick(v, getAdapterPosition());
            });

        }

    }
}