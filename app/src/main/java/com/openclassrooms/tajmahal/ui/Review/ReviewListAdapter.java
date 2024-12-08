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
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * ReviewListAdapter is responsible for binding a list of reviews to a RecyclerView.
 * It displays each review with the username, comment, rating, and avatar image.
 * The adapter uses Glide to load the user's avatar image.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {

    private List<Review> reviewList = new ArrayList<>(); // List of reviews to be displayed in the RecyclerView

    /**
     * Constructor for the ReviewListAdapter that initializes the review list.
     * @param reviews The list of reviews to be displayed.
     */
    public ReviewListAdapter(List<Review> reviews) {
        if (reviews != null) {
            this.reviewList = reviews; // Initialize with the provided list if not null
        } else {
            this.reviewList = new ArrayList<>();  // Initialize as an empty list if reviews are null
        }
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder to be created.
     * Inflates the layout for each individual review item.
     * @param parent The parent view that the new item view will be attached to.
     * @param viewType The view type of the new view (if needed).
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ReviewListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the review item layout and return a new ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Binds the data from the review list to the ViewHolder for each individual item.
     * This method populates the review details into the respective UI components.
     * @param holder The ViewHolder that holds the views for an individual review item.
     * @param position The position in the list of reviews that should be displayed.
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.MyViewHolder holder, int position) {

        // Get the review at the given position in the list
        Review review = reviewList.get(position);

        // Set the review's comment and username into the TextViews
        holder.commentTextView.setText(review.getComment());
        holder.userInList.setText(review.getUsername());

        // Set the rating for the review using the RatingBar
        holder.ratingbarSetup.setRating(review.getRate());

        // Load the user's avatar image into the ImageView using Glide
        Glide.with(holder.itemView.getContext())
                .load(review.getPicture()) // Load the URL of the avatar image
                .circleCrop() // Crop the image to make it circular
                .into(holder.avatarView); // Set the image into the ImageView
    }

    /**
     * Returns the number of items in the review list.
     * @return The total number of reviews in the list.
     */
    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    /**
     * MyViewHolder is the ViewHolder class that holds the views for each individual review item.
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView userInList; // TextView for displaying the username
        public TextView commentTextView; // TextView for displaying the review comment
        public ImageView avatarView; // ImageView for displaying the user's avatar
        public RatingBar ratingbarSetup; // RatingBar for displaying the rating

        /**
         * Constructor for MyViewHolder. Initializes all the views in the item layout.
         * @param itemView The root view of the individual review item.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views by finding them by their IDs
            userInList = itemView.findViewById(R.id.userInList);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            avatarView = itemView.findViewById(R.id.avatarView);
            ratingbarSetup = itemView.findViewById(R.id.ratingbarSetup);
        }
    }
}
