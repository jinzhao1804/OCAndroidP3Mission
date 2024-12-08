package com.openclassrooms.tajmahal.ui.Review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.data.repository.ReviewRepository;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;
import java.util.ArrayList;


/**
 * ViewModel responsible for managing data related to reviews.
 * It interacts with the repository to obtain reviews and exposes them via LiveData.
 * This ViewModel is used to ensure the UI only interacts with live data and not directly with data sources.
 */
public class ReviewViewModel extends ViewModel {

    private final ReviewRepository reviewRepository; // Repository to fetch review data
    private final MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>(new ArrayList<>()); // LiveData holding the list of reviews

    /**
     * Initializes the ViewModel by creating a repository and loading the reviews.
     */
    public ReviewViewModel() {
        reviewRepository = new ReviewRepository(); // Initialize the ReviewRepository
        loadReviews(); // Load the reviews from the repository when the ViewModel is created
    }

    /**
     * Retrieves the reviews exposed via LiveData.
     * This method allows the UI to observe changes in the list of reviews.
     *
     * @return An instance of LiveData containing the current list of reviews.
     */
    public LiveData<List<Review>> getReviews() {
        return reviewsLiveData; // Return LiveData to observe the reviews
    }

    /**
     * Loads the reviews from the repository and publishes them via LiveData.
     * This method is called during the initialization of the ViewModel to load initial data.
     */
    private void loadReviews() {
        List<Review> reviews = reviewRepository.getReviews(); // Retrieve the list of reviews from the repository
        reviewsLiveData.postValue(reviews); // Update the LiveData with the list of reviews
    }

    /**
     * Adds a new review to the list and updates the LiveData if the review is valid.
     * A valid review must have a non-empty comment and a rating greater than 0.
     * The review is added to the beginning of the list.
     *
     * @param review The review to be added.
     * @return true if the review is successfully added, false if the review is invalid.
     */
    public boolean addReview(Review review) {
        // Validate the review: the comment must not be empty, and the rating must be greater than 0
        if (review.getComment().isEmpty() || review.getRate() <= 0) {
            return false; // Invalid review, return false
        }

        // Get the current list of reviews
        List<Review> currentReviews = new ArrayList<>(reviewsLiveData.getValue());
        currentReviews.add(0, review); // Add the new review at the start of the list

        // Update the LiveData with the new list of reviews
        reviewsLiveData.setValue(currentReviews);
        return true; // Successfully added the review
    }
}
