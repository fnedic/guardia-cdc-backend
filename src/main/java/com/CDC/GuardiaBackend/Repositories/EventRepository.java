package com.CDC.GuardiaBackend.Repositories;

import com.CDC.GuardiaBackend.Entities.Event;
import com.CDC.GuardiaBackend.Enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {
    Optional<Event> findByStartDate (Date startDate);
    Optional<Event> findByEndDate (Date endDate);

    List<Event> findByUserId (String userId);
    List<Event> findByEventStatus (EventStatus eventStatus);

    @Query("SELECT e FROM Event e WHERE " +
            "(e.startDate BETWEEN :start AND :end) OR " +
            "(e.endDate BETWEEN :start AND :end)")
    List<Event> findByStartDateBetween(
            @Param("start") Date start,
            @Param("end") Date end
    );
}
