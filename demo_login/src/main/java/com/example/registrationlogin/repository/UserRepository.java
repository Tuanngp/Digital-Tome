package com.example.registrationlogin.repository;

import com.example.registrationlogin.dto.ProfileDto;
import com.example.registrationlogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByToken(String token);
    User findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("select new com.example.registrationlogin.dto.ProfileDto(u.id,u.email, u.address, u.fullName, u.phone, u.age) from User u where u.username = :username")
    ProfileDto findByUser(@Param("username") String username);
}
