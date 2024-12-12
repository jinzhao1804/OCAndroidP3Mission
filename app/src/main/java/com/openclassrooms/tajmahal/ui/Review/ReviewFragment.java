package com.openclassrooms.tajmahal.ui.Review;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.openclassrooms.tajmahal.databinding.FragmentReviewBinding;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * ReviewFragment is responsible for displaying the list of reviews for the restaurant
 * and allowing users to submit new reviews with a rating.
 * It uses a ViewModel to manage review data and LiveData to observe changes in the data.
 * This fragment provides UI components to display reviews, take user input, and navigate back to the previous screen.
 */
@AndroidEntryPoint  // Enable Hilt to inject the ViewModel
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding; // Data binding object for the fragment layout
    private ReviewViewModel reviewViewModel; // ViewModel for managing reviews
    private float myRating = 0; // Stores the rating selected by the user

    /**
     * Default constructor for ReviewFragment. No arguments required.
     */
    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the layout for the fragment using ViewBinding.
     * @param inflater The LayoutInflater object to inflate the layout.
     * @param container The parent view group.
     * @param savedInstanceState The saved state of the fragment (if any).
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return the root view for this fragment
    }

    /**
     * Called when the view has been created.
     * Initializes the toolbar, RecyclerView, and observes the review data.
     * Also sets up listeners for the rating bar and review submission button.
     * @param view The root view of the fragment.
     * @param savedInstanceState The saved state of the fragment (if any).
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the Toolbar navigation click listener (for back navigation)
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // Pop the current fragment from the back stack
                } else {
                    // If there are no fragments in the back stack, finish the activity
                    getActivity().finish();
                }
            }
        });

        // Initialize the ViewModel to manage review data
        reviewViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);

        // Set up RecyclerView with LinearLayoutManager for displaying reviews
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe the reviews LiveData and update the UI when data changes
        reviewViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            if (reviews != null) {
                // Set the adapter for RecyclerView with the list of reviews
                ReviewListAdapter adapter = new ReviewListAdapter(reviews);
                binding.recyclerView.setAdapter(adapter);
            }
        });

        // Set up listener for the rating bar to capture user rating
        setupRatingBar();

        // Set up the button click listener for submitting a new review
        binding.validateReviewButton.setOnClickListener(v -> saveNewReview());
    }

    /**
     * Sets up the listener for the rating bar. When the rating is changed,
     * the selected rating is stored and displayed as a toast.
     */
    private void setupRatingBar(){
        binding.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myRating = rating; // Store the selected rating
                Toast.makeText(getContext(), "Rating: " + myRating, Toast.LENGTH_SHORT).show(); // Display the rating as a toast
            }
        });
    }

    /**
     * Saves a new review by collecting user input and adding it to the list of reviews.
     * A new Review object is created and added to the ViewModel's LiveData, which will automatically
     * update the UI to display the new review.
     */
    private void saveNewReview() {
        // Retrieve user input for the review
        String currentUser = binding.activeUser.getText().toString();
        String reviewText = binding.editText.getText().toString();
        int rating = Math.round(binding.rating.getRating()); // Get the rounded rating from the rating bar

        // Create a new Review object
        Review newReview = new Review(currentUser, "https://xsgames.co/randomusers/assets/avatars/female/0.jpg", reviewText, rating);



        // Validate the review data before saving it
        if (validateReviewData()){
            // Add the new review to the list and update the LiveData
            reviewViewModel.addReview(newReview);

            // Clear the input fields after saving the review
            binding.editText.setText(""); // Clear the review text
            binding.rating.setRating(0); // Reset the rating bar
        }
    }

    /**
     * Validates the review data to ensure that a rating is selected and a comment is provided.
     * Displays a toast message if the data is invalid.
     * @return true if the data is valid, false otherwise.
     */
    private boolean validateReviewData() {
        String reviewText = binding.editText.getText().toString();
        float rating = binding.rating.getRating();

        if (reviewText.isEmpty()) {
            Toast.makeText(getContext(), "Review comment cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rating == 0) {
            Toast.makeText(getContext(), "Please provide a star rating", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Data is valid
    }
}
