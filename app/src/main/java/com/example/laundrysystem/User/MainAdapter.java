package com.example.laundrysystem.User;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MainModel model);
    }

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.bind(model, listener);

        Calendar calendar = Calendar.getInstance(); // kwaon mo anu oras subong
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 16|| hour <= 9) { //calculate mo kung nag lapaw na sang 10pm sang gabe disable na
            holder.itemView.setEnabled(false); //asta 9am
            holder.itemView.setOnClickListener(null);
           holder.itemView.setBackgroundColor(Color.parseColor("#FF0000"));

        } else {
            holder.itemView.setEnabled(true); //open mo nmn sa daun 9 am onwards
            holder.itemView.setBackgroundColor(Color.parseColor("#3DC2EC"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(model);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView laundryName, laundryPrice, laundryLocation;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            laundryName = itemView.findViewById(R.id.laundryName);
            laundryPrice = itemView.findViewById(R.id.laundryPrice);
            laundryLocation = itemView.findViewById(R.id.laundryLocation);
        }

        public void bind(final MainModel model, final OnItemClickListener listener) {
            laundryName.setText(model.getShop_name());
            laundryPrice.setText(model.getContact_number());
            laundryLocation.setText(model.getLocation());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(model);
                    }
                }
            });
        }
    }
}
