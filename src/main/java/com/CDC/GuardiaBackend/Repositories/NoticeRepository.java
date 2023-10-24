package com.CDC.GuardiaBackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CDC.GuardiaBackend.Entities.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String>{

}
