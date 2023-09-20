package com.CDC.GuardiaBackend.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CDC.GuardiaBackend.Entities.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, String>{
    Optional<Video> findByLink(String link);
}
