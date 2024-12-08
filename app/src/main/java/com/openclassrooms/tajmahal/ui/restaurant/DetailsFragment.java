package com.openclassrooms.tajmahal.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.data.service.RestaurantFakeApi;
import com.openclassrooms.tajmahal.databinding.FragmentDetailsBinding;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;
import com.openclassrooms.tajmahal.ui.Review.ReviewFragment;
import com.openclassrooms.tajmahal.ui.Review.ReviewViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * DetailsFragment is the entry point of the application and serves as the primary UI.
 * It displays details about a restaurant and provides functionality to open its location
 * in a map, call its phone number, or view its website.
 * <p>
 * This class uses {@link FragmentDetailsBinding} for data binding to its layout and
 * {@link DetailsViewModel} to interact with data sources and manage UI-related data.
 */
@AndroidEntryPoint
public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding; // Data binding object
    private DetailsViewModel detailsViewModel; // ViewModel for restaurant data
    private ReviewViewModel reviewViewModel; // ViewModel for reviews

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return the root view for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(); // Set up the UI appearance
        setupViewModel(); // Initialize ViewModel

        // Initialize the shared ViewModel for reviews
        reviewViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);

        // Observe the LiveData for changes in reviews and update UI accordingly
        reviewViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            if (reviews != null) {
                binding.numberOfRating.setText("(" + reviews.size() + ")");
                float averageRating = calculateAverageRating(reviews); // Calculate the average rating
                binding.ratingMain.setRating(averageRating);
                binding.averageRating.setText(String.format(Locale.getDefault(), "%.1f", averageRating));
            }
        });

        // Observe restaurant data changes and update UI accordingly
        detailsViewModel.getTajMahalRestaurant().observe(requireActivity(), this::updateUIWithRestaurant);
    }

    /**
     * Set up the UI elements such as making the status bar transparent
     */
    private void setupUI() {
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT); // Make status bar transparent
    }

    /**
     * Initialize the ViewModel for restaurant details
     */
    private void setupViewModel() {
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    /**
     * Updates the UI with the details of the given restaurant.
     * @param restaurant The restaurant whose details will be shown.
     */
    @SuppressLint("SetTextI18n")
    private void updateUIWithRestaurant(Restaurant restaurant) {
        if (restaurant == null) return;

        // Set the UI elements with the restaurant's data
        binding.tvRestaurantName.setText(restaurant.getName());
        binding.tvRestaurantDay.setText(detailsViewModel.getCurrentDay(requireContext()));
        binding.tvRestaurantType.setText(String.format("%s %s", getString(R.string.restaurant), restaurant.getType()));
        binding.tvRestaurantHours.setText(restaurant.getHours());
        binding.tvRestaurantAddress.setText(restaurant.getAddress());
        binding.tvRestaurantWebsite.setText(restaurant.getWebsite());
        binding.tvRestaurantPhoneNumber.setText(restaurant.getPhoneNumber());
        binding.chipOnPremise.setVisibility(restaurant.isDineIn() ? View.VISIBLE : View.GONE);
        binding.chipTakeAway.setVisibility(restaurant.isTakeAway() ? View.VISIBLE : View.GONE);

        // Set up listeners for buttons
        binding.buttonAdress.setOnClickListener(v -> openMap(restaurant.getAddress()));
        binding.buttonPhone.setOnClickListener(v -> dialPhoneNumber(restaurant.getPhoneNumber()));
        binding.buttonWebsite.setOnClickListener(v -> openBrowser(restaurant.getWebsite()));

        // Handle click on the "Laisser un avis" button to open the review fragment
        binding.laisserUnavis.setText("Laisser un avis");
        binding.laisserUnavis.setOnClickListener(v -> {
            ReviewFragment fragmentB = new ReviewFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragmentB);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    /**
     * Calculate the average rating from a list of reviews.
     * @param reviews The list of reviews.
     * @return The average rating.
     */
    private float calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0F;
        }

        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRate(); // Sum all the ratings
        }

        return (float) (sum / reviews.size()); // Return the average
    }

    /**
     * Open Google Maps with the given address.
     * @param address The address to display on the map.
     */
    private void openMap(String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent); // Start the map activity if Google Maps is installed
        } else {
            Toast.makeText(requireActivity(), R.string.maps_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Dial the given phone number using the dialer app.
     * @param phoneNumber The phone number to dial.
     */
    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent); // Start the dialer activity
        } else {
            Toast.makeText(requireActivity(), R.string.phone_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open the given website URL in a browser.
     * @param websiteUrl The URL of the website to open.
     */
    private void openBrowser(String websiteUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent); // Open the browser if available
        } else {
            Toast.makeText(requireActivity(), R.string.no_browser_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates and returns a new instance of DetailsFragment.
     * @return A new instance of DetailsFragment.
     */
    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }
}
