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

@AndroidEntryPoint  // Enable Hilt to inject the ViewModel
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel reviewViewModel;
    private float myRating = 0;


    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Set up the Toolbar
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the previous screen
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // Pop the back stack entry
                } else {
                    // Optionally handle the case where there's no fragment to go back to
                    // For example, you might want to exit the activity
                    getActivity().finish();
                }
            }
        });

        // Initialize ViewModel
        reviewViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);

        // Initialize RecyclerView with LinearLayoutManager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe the reviews LiveData
        reviewViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            if (reviews != null) {
                // Set the data in the adapter
                ReviewListAdapter adapter = new ReviewListAdapter(reviews);
                binding.recyclerView.setAdapter(adapter); // Correctly use the binding object here

            }
        });

        // Set up the RatingBar listener
        setupRatingBar();

        // Add new review on button click
        binding.validateReviewButton.setOnClickListener(v -> saveNewReview());
    }


    private void setupRatingBar(){
        binding.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myRating = v; // Store the selected rating
                Toast.makeText(getContext(), "Rating: " + myRating, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void saveNewReview() {
        // Retrieve user input
        String currentUser = binding.activeUser.getText().toString();
        String reviewText = binding.editText.getText().toString();
        int rating = Math.round(binding.rating.getRating());

        // Create a new Review object
        Review newReview = new Review(currentUser, "https://xsgames.co/randomusers/assets/avatars/female/0.jpg", reviewText, rating);

        // Get the current list of reviews
        List<Review> currentReviews = reviewViewModel.getReviews().getValue();


        // Create a new mutable list if the current list is unmodifiable or null
        List<Review> updatedReviews;
        if (currentReviews == null) {
            updatedReviews = new ArrayList<>();
        } else {
            updatedReviews = new ArrayList<>(currentReviews); // Create a new mutable list
        }

        if (validateReviewData()){
            // Add the new review to the mutable list
            //updatedReviews.add(newReview);

            // Update the LiveData with the new list
            reviewViewModel.addReview(newReview);

            // Clear the EditText after saving the review
            binding.editText.setText(""); // This clears the text in the EditText

            binding.rating.setRating(0);
        }
    }

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
