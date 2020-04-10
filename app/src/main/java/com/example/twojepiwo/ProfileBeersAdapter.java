package com.example.twojepiwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeoutException;

public class ProfileBeersAdapter extends RecyclerView.Adapter<ProfileBeersAdapter.ProfileBeersViewHolder> {

    private Context mContext;
    private ItemClickListener mItemClickListener;

    public ProfileBeersAdapter(Context context,ItemClickListener listener)
    {
        mContext = context;
        mItemClickListener = listener;
    }


    @NonNull
    @Override
    public ProfileBeersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_profile_beer_layout,parent,false);
        return new ProfileBeersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileBeersViewHolder holder, int position) {
        //TODO: Implement a list of beers on profile
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ItemClickListener
    {
        void onItemClickListener(int itemId);
    }

    class ProfileBeersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView imageView;
        private TextView beerName;
        private TextView beerLevel;

        public ProfileBeersViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.single_beer_image_view);
            beerName = itemView.findViewById(R.id.single_beer_name);
            beerLevel = itemView.findViewById(R.id.single_beer_level);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
