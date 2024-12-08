package com.openclassrooms.tajmahal.data.service;

import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;  // Hilt Injection annotation

/**
 * A mock implementation of the {@link RestaurantApi} for testing and development purposes.
 */
public class RestaurantFakeApi implements RestaurantApi {

    // A list of hard-coded reviews for testing purposes
    List<Review> reviews = Arrays.asList(
            new Review("Ranjit Singh", "https://xsgames.co/randomusers/assets/avatars/male/71.jpg",
                    "Service très rapide et nourriture délicieuse, nous mangeons ici chaque week-end, c'est très rapide et savoureux. Continuez ainsi!", 5),
            new Review("Martyna Siddeswara", "https://xsgames.co/randomusers/assets/avatars/female/31.jpg",
                    "Un service excellent et des plats incroyablement savoureux. Nous sommes vraiment satisfaits de notre expérience au restaurant.", 4),
            new Review("Komala Alanazi", "https://xsgames.co/randomusers/assets/avatars/male/46.jpg",
                    "La cuisine est délicieuse et le service est également excellent. Le propriétaire est très sympathique et veille toujours à ce que votre repas soit satisfaisant. Cet endroit est un choix sûr!", 5),
            new Review("David John", "https://xsgames.co/randomusers/assets/avatars/male/67.jpg",
                    "Les currys manquaient de diversité de saveurs et semblaient tous à base de tomates. Malgré les évaluations élevées que nous avons vues et nos attentes, nous avons été déçus.", 2),
            new Review("Emilie Hood", "https://xsgames.co/randomusers/assets/avatars/female/20.jpg",
                    "Très bon restaurant Indien ! Je recommande.", 4)
    );

    /**
     * Constructor annotated with @Inject to allow Hilt to inject this class.
     */
    @Inject
    public RestaurantFakeApi() {
        // This constructor can now be injected by Hilt if needed
    }

    /**
     * Retrieves a hard-coded {@link Restaurant} object for the "Taj Mahal".
     *
     * @return The hard-coded {@link Restaurant} object for the "Taj Mahal".
     */
    @Override
    public Restaurant getRestaurant() {
        return new Restaurant("Taj Mahal", "Indien", "11h30 - 14h30・18h30 - 22h00",
                "12 Avenue de la Brique - 75010 Paris", "http://www.tajmahal.fr", "06 12 34 56 78",
                true, true);
    }

    /**
     * Retrieves a hard-coded {@link Review} list for the "Taj Mahal".
     *
     * @return The hard-coded list of {@link Review} for the "Taj Mahal".
     */
    @Override
    public List<Review> getReviews() {
        return reviews;
    }
}
