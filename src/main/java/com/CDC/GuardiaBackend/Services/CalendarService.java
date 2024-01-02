package com.CDC.GuardiaBackend.Services;

import com.CDC.GuardiaBackend.Configs.UserAuthenticationProvider;
import com.CDC.GuardiaBackend.Entities.Event;
import com.CDC.GuardiaBackend.Entities.User;
import com.CDC.GuardiaBackend.Enums.EventStatus;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Repositories.EventRepository;
import com.CDC.GuardiaBackend.Repositories.UserRepository;
import com.CDC.GuardiaBackend.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    public void validateEvent(Event event) {
        List<Event> userEventList = eventRepository.findByUserId(event.getUserId());

        LocalDateTime newStartDate = DateUtils.convertToLocalDateTime(event.getStartDate());
        LocalDateTime newEndDate = DateUtils.convertToLocalDateTime(event.getEndDate());

        Optional<Event> matchingEvent = userEventList.stream()
                .filter(existingEvent ->
                        DateUtils.convertToLocalDateTime(existingEvent.getStartDate()).isEqual(newStartDate) &&
                                DateUtils.convertToLocalDateTime(existingEvent.getEndDate()).isEqual(newEndDate))
                .findFirst();

        if (matchingEvent.isEmpty()) {
            assignUserData(event);
        } else if (matchingEvent.isPresent()) {
            if (!matchingEvent.get().getColor().equals(event.getColor())) {
                assignUserData(event);
            } else {
                throw  new AppException("Evento existente para el usuario seleccionado", HttpStatus.CONFLICT);
            }
        }
    }


    public void assignUserData (Event event) {
        Optional<User> optional = userRepository.findById(event.getUserId());
        if (optional.isPresent()) {
            Event newEvent = event;
            newEvent.setTitle(optional.get().getName()+" "+optional.get().getLastname());
            newEvent.setColor(event.getColor());
            newEvent.setStartDate(event.getStartDate());
            newEvent.setEndDate(event.getEndDate());
            if (event.getEventStatus().equals("CHANGED") || event.getEventStatus().equals("ASSIGNED") || event.getEventStatus().equals("REQUESTED") || event.getEventStatus().equals("CHANGED")) {
                newEvent.setEventStatus(event.getEventStatus());
            }
            eventRepository.save(event);
        } else {
            throw new AppException("Error al guardar evento", HttpStatus.BAD_GATEWAY);
        }
    }

    public void requestChange (String id) {
        try {
            Optional<Event> optional = eventRepository.findById(id);
            if (optional.isPresent()) {
                Event toChangeEvent = optional.get();
                toChangeEvent.setEventStatus(EventStatus.REQUESTED);
                eventRepository.save(toChangeEvent);
            }
        } catch (Exception e) {
            throw new AppException("Error al solicitar cambio de guardia.", HttpStatus.BAD_REQUEST);
        }
    }

    public void requestCancel (String id) {
        try {
            Optional<Event> optional = eventRepository.findById(id);
            if (optional.isPresent()) {
                Event toChangeEvent = optional.get();
                toChangeEvent.setEventStatus(EventStatus.ASSIGNED);
                eventRepository.save(toChangeEvent);
            }
        } catch (Exception e) {
            throw new AppException("Error al solicitar cancelación de cambio de guardia.", HttpStatus.BAD_REQUEST);
        }
    }

    public void requestChange (String authHeader, String id) {
        try {
            Optional<User> optionalUser = userRepository.findById(getUserId(authHeader));
            Optional<Event> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isPresent() && optionalUser.isPresent()) {
                Event toChangeEvent = optionalEvent.get();
                User newUser = optionalUser.get();
                toChangeEvent.setOldOwnerId(toChangeEvent.getUserId());
                toChangeEvent.setOldOwnerTitle(toChangeEvent.getTitle());
                toChangeEvent.setEventStatus(EventStatus.CHANGED);
                toChangeEvent.setUserId(newUser.getId());
                toChangeEvent.setTitle(newUser.getName()+" "+newUser.getLastname());
                eventRepository.save(toChangeEvent);
            }
        } catch (Exception e) {
            throw new AppException("Error al tomar cambio de guardia.", HttpStatus.BAD_REQUEST);
        }
    }

    public void requestAccept (String id) {
        try {
            Optional<Event> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isPresent()) {
                Event toChangeEvent = optionalEvent.get();
                toChangeEvent.setEventStatus(EventStatus.APPROVED);
                eventRepository.save(toChangeEvent);
            }
        } catch (Exception e) {
            throw new AppException("Error al validar cambio de guardia.", HttpStatus.BAD_REQUEST);
        }
    }

    public void requestChangeCancel (String id) {
        try {
            Optional<Event> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isPresent()) {
                Event toChangeEvent = optionalEvent.get();
                toChangeEvent.setTitle(toChangeEvent.getOldOwnerTitle());
                toChangeEvent.setUserId(toChangeEvent.getOldOwnerId());
                toChangeEvent.setOldOwnerTitle(null);
                toChangeEvent.setOldOwnerId(null);
                toChangeEvent.setEventStatus(EventStatus.ASSIGNED);
                eventRepository.save(toChangeEvent);
            }
        } catch (Exception e) {
            throw new AppException("Error al cancelar cambio de guardia.", HttpStatus.BAD_REQUEST);
        }
    }

    public String getUserId (String authHeader) {
        try {
            String token = authHeader.substring(7);
            if (authHeader.length() >= 7 && authHeader.startsWith("Bearer ")) {
                UserDto userDto = userAuthenticationProvider.getUser(token);
                return userDto.getId();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public class DateUtils {

        public static LocalDateTime convertToLocalDateTime(Date date) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }
}
