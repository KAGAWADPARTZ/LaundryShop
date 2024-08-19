package com.example.laundrysystem.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.LaundryOwners.LaundryMainModel;
import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UserStatusMainAdapter extends FirebaseRecyclerAdapter<LaundryMainModel, UserStatusMainAdapter.myViewHolder> {

    private OnItemClickListener listener;

    public UserStatusMainAdapter(@NonNull FirebaseRecyclerOptions<LaundryMainModel> options) {
        super(options);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(LaundryMainModel model, String key);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull LaundryMainModel model) {
        holder.userName.setText(model.getShop_name());
        holder.userAddress.setText(model.getLocation());
//        holder.contactNumber.setText(model.getContactNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(model, getRef(position).getKey());
                }
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_status_item_list, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userAddress,contactNumber;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userAddress = itemView.findViewById(R.id.userAddress);
//            contactNumber = itemView.findViewById(R.id.contactNumber);
        }
    }

}
