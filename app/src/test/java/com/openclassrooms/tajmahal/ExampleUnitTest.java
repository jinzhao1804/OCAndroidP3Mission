package com.openclassrooms.tajmahal;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.openclassrooms.tajmahal.domain.model.Review;
import com.openclassrooms.tajmahal.ui.Review.ReviewViewModel;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExampleUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ReviewViewModel viewModel;

    @Mock
    private Observer<List<Review>> observer;

    @Before
    public void setUp() {
        // Observe LiveData
        viewModel = new ReviewViewModel();
        observer = mock(Observer.class);
        viewModel.getReviews().observeForever(observer);
    }

    @Test
    public void addReview_shouldAddReviewToList() {
        // Given a new review
        Review review = new Review("John Doe", "https://example.com/image.jpg", "Great place!", 5);

        // When the review is added
        boolean result = viewModel.addReview(review);

        // Then the review list should be updated
        List<Review> expectedReviews = viewModel.getReviews().getValue();
        expectedReviews.add(review);

        assertTrue(result); // Check that the review was added
        assertEquals(expectedReviews, viewModel.getReviews().getValue());
    }

    @Test
    public void testAddReview_InvalidReview() {
        // Given: an invalid review (empty comment and 0 rating)
        Review invalidReview = new Review("User4", "https://image4.jpg", "", 0);

        // Save the current size of the reviews list
        int initialSize = viewModel.getReviews().getValue().size();

        // When: try to add the invalid review
        boolean result = viewModel.addReview(invalidReview);

        // Then: the result should be false, and the reviews list size should remain unchanged
        assertFalse(result);  // The review should not be added
        assertEquals(initialSize, viewModel.getReviews().getValue().size());  // List size should be the same
    }


    @Test
    public void addReview_shouldNotAddReviewWithEmptyComment() {
        // Given
        Review invalidReview = new Review("User5", "https://image5.jpg", "", 4); // Empty comment, valid rating

        // Save the current size of the reviews list
        int initialSize = viewModel.getReviews().getValue().size();

        // When: try to add the invalid review
        boolean result = viewModel.addReview(invalidReview);

        // Then: the result should be false, and the reviews list size should remain unchanged
        assertFalse(result);  // The review should not be added
        assertEquals(initialSize, viewModel.getReviews().getValue().size());  // List size should be the same
    }

    @Test
    public void addReview_shouldNotAddReviewWithEmptyRating() {
        // Given
        Review invalidReview = new Review("User5", "https://image5.jpg", "some review here", 0 ); // Empty comment, valid rating

        // Save the current size of the reviews list
        int initialSize = viewModel.getReviews().getValue().size();

        // When: try to add the invalid review
        boolean result = viewModel.addReview(invalidReview);

        // Then: the result should be false, and the reviews list size should remain unchanged
        assertFalse(result);  // The review should not be added
        assertEquals(initialSize, viewModel.getReviews().getValue().size());  // List size should be the same
    }

    @Test
    public void addReview_shouldNotifyObserver() {
        // Given a new review
        Review review = new Review("Jane Doe", "https://example.com/image.jpg", "Nice food!", 4);

        // When the review is added
        boolean result = viewModel.addReview(review);

        // Then the review should be added to the list, and the observer should be notified
        assertTrue(result); // Check that the review was added

        // Verify that the observer's onChanged method is called with the updated list
        verify(observer).onChanged(viewModel.getReviews().getValue());
    }

    @Test
    public void addReview_shouldAddReviewAtFirstPosition() {
        int initialSize = viewModel.getReviews().getValue().size();
        // Given: Add a first review
        Review firstReview = new Review("John Doe", "Great place!", "Nice food!", 5);
        viewModel.addReview(firstReview);

        // When: Add another review
        Review secondReview = new Review("Jane Doe", "Good service!", "Nice food!",4);
        viewModel.addReview(secondReview);

        // Then: Verify the first review is still at the first position
        List<Review> reviews = viewModel.getReviews().getValue();
        assertEquals(initialSize + 2, reviews.size());
        assertEquals(secondReview, reviews.get(0));  // Ensure first review is at index 0
    }

}
