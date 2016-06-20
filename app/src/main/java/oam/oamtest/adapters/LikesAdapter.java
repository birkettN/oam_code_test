package oam.oamtest.adapters;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import oam.oamtest.R;
import oam.oamtest.models.ListingModel;
import oam.oamtest.ui.CircleTransform;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ListingViewHolder> {

    List<ListingModel> mListingModels;
    Context mContext;

    public LikesAdapter(List<ListingModel> listingModels, Context context) {
        this.mListingModels = listingModels;
        this.mContext = context;
    }


    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        ImageView mImage;
        TextView mMileage;
        TextView mYear;
        TextView mContact;
        TextView mPrice;

        ListingViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.list_item_image);

            mMileage = (TextView) itemView.findViewById(R.id.list_item_mileage);
            mYear = (TextView) itemView.findViewById(R.id.list_item_year);
            mContact = (TextView) itemView.findViewById(R.id.list_item_contact);
            mPrice = (TextView) itemView.findViewById(R.id.list_item_price);

        }
    }


    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.likes_list_item_layout, viewGroup, false);
        return new ListingViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mListingModels.size();
    }


    @Override
    public void onBindViewHolder(ListingViewHolder listingViewHolder, int position) {
        listingViewHolder.mItemView.setTag(position);
        listingViewHolder.mPrice.setText(String.format("Price: %s %s", mListingModels.get(position).currency_symbol, mListingModels.get(position).price));
        listingViewHolder.mMileage.setText(String.format("%s", mListingModels.get(position).title));
        listingViewHolder.mContact.setText(String.format("Contact: %s", mListingModels.get(position).mobile_number));
        listingViewHolder.mYear.setText(String.format("Year: %s", mListingModels.get(position).year));

        listingViewHolder.mImage.setImageDrawable(null);

        if (mListingModels.get(position).default_image != null && mListingModels.get(position).default_image.length() > 0) {
            String imgUrl = mListingModels.get(position).default_image.replaceAll("\\\\/", "/");
            Picasso.with(mContext)
                    .load(imgUrl)
                    .fit().centerCrop()
                    .transform(new CircleTransform())
                    .tag(imgUrl)
                    .into(listingViewHolder.mImage);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}