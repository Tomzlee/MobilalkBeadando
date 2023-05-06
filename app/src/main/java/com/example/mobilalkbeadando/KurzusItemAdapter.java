package com.example.mobilalkbeadando;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class KurzusItemAdapter extends RecyclerView.Adapter<KurzusItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<KurzusItem> mKurzusItemsData;
    private ArrayList<KurzusItem> mKurzusItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;


    KurzusItemAdapter(Context context, ArrayList<KurzusItem> itemsData){

        this.mKurzusItemsData = itemsData;
        this.mKurzusItemsDataAll = itemsData;
        this.mContext = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( KurzusItemAdapter.ViewHolder holder, int position) {
        KurzusItem currentItem = mKurzusItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mKurzusItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return kurzusFilter;
    }

    private Filter kurzusFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<KurzusItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length()==0) {
                results.count = mKurzusItemsDataAll.size();
                results.values = mKurzusItemsDataAll;
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (KurzusItem item : mKurzusItemsDataAll){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mKurzusItemsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;
        public ViewHolder( View itemView) {
            super(itemView);

              mTitleText = itemView.findViewById(R.id.itemTitle);
              mInfoText = itemView.findViewById(R.id.subTitle);
              mPriceText = itemView.findViewById(R.id.price);
              mItemImage = itemView.findViewById(R.id.itemImage);
              mRatingBar = itemView.findViewById(R.id.ratingBar);


        }

        public void bindTo(KurzusItem currentItem) {
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getPrice());
            mRatingBar.setRating(currentItem.getRatedInfo());

            Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(view -> ((FoOldalActivity)mContext).updateAlertIcon(currentItem));
        }
    }
}

