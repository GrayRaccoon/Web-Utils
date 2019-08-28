package com.grayraccoon.webutils.config.beans.repository;

import com.grayraccoon.webutils.config.beans.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {
    UsersEntity findFirstByEmailOrUsername(String email, String username);
}
