package com.openclassrooms.tajmahal.ui.Review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.data.repository.ReviewRepository;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {

    private List<Review> reviewList = new ArrayList<>(); // Initialize as an empty list

    public ReviewListAdapter(List<Review> reviews) {
        if (reviews != null) {
            this.reviewList = reviews;
        } else {
            this.reviewList = new ArrayList<>();  // Initialize as an empty list if reviews are null
        }
    }

    @NonNull
    @Override
    public ReviewListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.MyViewHolder holder, int position) {

        Review review = reviewList.get(position);
        holder.commentTextView.setText(review.getComment());
        holder.userInList.setText(review.getUsername());
        holder.ratingbarSetup.setRating(review.getRate());
        // Load review user avatar using Glide or another image loading library
        Glide.with(holder.itemView.getContext())
                .load(review.getPicture())
                .circleCrop()// Assuming review.getPicture() returns a URL
                .into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView userInList;
        public TextView commentTextView;
        public ImageView avatarView;
        public RatingBar ratingbarSetup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userInList = itemView.findViewById(R.id.userInList);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            avatarView = itemView.findViewById(R.id.avatarView);
            ratingbarSetup = itemView.findViewById(R.id.ratingbarSetup);

        }
    }
}
