package com.example.demo.database;

import com.example.demo.model.database.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDao extends JpaRepository<UserEntity, Long> {
    UserEntity getUserEntitiesByUsername(String username);
    UserEntity findUserEntityById(long id);
}
