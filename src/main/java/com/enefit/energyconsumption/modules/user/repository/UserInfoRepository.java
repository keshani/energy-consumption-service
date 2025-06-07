package com.enefit.energyconsumption.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enefit.energyconsumption.modules.user.models.entity.User;

/**
 * Repository to handle all the City Entity related CRUD operations
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Repository
public interface UserInfoRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String username);
}

