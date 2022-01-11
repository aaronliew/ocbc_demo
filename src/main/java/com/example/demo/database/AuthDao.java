package com.example.demo.database;

import com.example.demo.model.database.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDao extends JpaRepository<UserEntity, Long> {
}
