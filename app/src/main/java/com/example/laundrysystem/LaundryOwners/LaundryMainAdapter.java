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
import com.google.firebase.database.DatabaseReference;


public class LaundryMainAdapter extends FirebaseRecyclerAdapter<LaundryMainModel, LaundryMainAdapter.myViewHolder> {

    private OnItemClickListener listener;

    public LaundryMainAdapter(@NonNull FirebaseRecyclerOptions<LaundryMainModel> options) {
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
        holder.userName.setText(model.getName());
        holder.userAddress.setText(model.getAddress());
        holder.contactNumber.setText(model.getContactNumber());
        holder.status.setText(model.getStatus());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item_list, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userAddress,contactNumber,status;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userAddress = itemView.findViewById(R.id.userAddress);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            status = itemView.findViewById(R.id.txtViewOnline);
        }
    }

    public void updateStatus(String key, String newStatus) {
        DatabaseReference ref = getRef(Integer.parseInt(getSnapshots().getSnapshot(getItemPosition(key)).getKey()));
        ref.child("status").setValue(newStatus);
    }

    private int getItemPosition(String key) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getRef(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }


}