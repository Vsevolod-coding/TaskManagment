package com.finalproject.TaskManagement.repository;

import com.finalproject.TaskManagement.entities.User;
import com.finalproject.TaskManagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserRole(UserRole userRole);

    Optional<User> findFirstByEmail(String email);
}
