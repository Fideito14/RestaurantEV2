package com.restaurant.authusuarios.userservice.repository;

import com.restaurant.authusuarios.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
