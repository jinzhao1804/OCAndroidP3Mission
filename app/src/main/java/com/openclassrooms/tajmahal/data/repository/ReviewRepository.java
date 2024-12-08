package com.openclassrooms.tajmahal.data.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.tajmahal.data.service.RestaurantFakeApi;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;  // Add this to inject dependencies

/**
 * Repository class for managing review data.
 */
public class ReviewRepository {

    /**
     * Retrieves a list of reviews.
     * This should be replaced with actual data fetching logic (e.g., from a database or API).
     *
     * @return A list of reviews.
     */
    public List<Review> getReviews() {
        // Replace with actual implementation, such as fetching from a database or network.
        return (List<Review>) new RestaurantFakeApi().getReviews(); // Use your fake API or actual data source
    }
}

