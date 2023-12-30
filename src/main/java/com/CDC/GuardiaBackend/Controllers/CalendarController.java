package com.CDC.GuardiaBackend.Controllers;

import com.CDC.GuardiaBackend.Configs.UserAuthenticationProvider;
import com.CDC.GuardiaBackend.Entities.Event;
import com.CDC.GuardiaBackend.Enums.EventStatus;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.EventRepository;
import com.CDC.GuardiaBackend.Services.CalendarService;
import com.CDC.GuardiaBackend.Services.UserService;
import com.CDC.GuardiaBackend.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private UserService userService;

    @PostMapping("/add-event")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {

        try {
            if (event.getUserId() != null) {
                calendarService.validateEvent(event);
                return new ResponseEntity<>("Evento creado correcatmente", HttpStatus.OK);
            } else {
                String mssg = "Error al guardar evento en base de datos!";
                return new ResponseEntity<>(mssg, HttpStatus.BAD_REQUEST);
            }
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (Exception e) {
            String mssg = "ERROR: controller exception!";
            throw new AppException("mssg", HttpStatus.FOUND);
        }
    }

    @PostMapping("/publish-events")
    public ResponseEntity<?> publishEvents(@RequestBody List<Event> events) {

        try {
            for (Event event : events) {
                event.setEventStatus(EventStatus.ASSIGNED);
            }
            eventRepository.saveAll(events);
            return ResponseEntity.ok("Eventos publicados correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al actualizar eventos");
        }
    }

    @GetMapping("/get-events")
    public ResponseEntity<?> getEvents(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));

        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            List<Event> events = eventRepository.findByStartDateBetween(startDate, endDate);
            if (!events.isEmpty()) {
                return ResponseEntity.ok(events);
            } else {
                return null;
            }
        } catch (ParseException e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/get-unpublished-events")
    public ResponseEntity<?> getUnpublishedEvents(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));

        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            List<Event> events = eventRepository.findByStartDateBetween(startDate, endDate);
            List<Event> unpublishedEvents = events.stream()
                    .filter(event -> event.getEventStatus().toString().equals("CREATED"))
                    .collect(Collectors.toList());
            if (!events.isEmpty()) {
                return ResponseEntity.ok(unpublishedEvents);
            } else {
                return null;
            }
        } catch (ParseException e) {
            throw new AppException("Error al traer eventos no publicados", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/get-user-events")
    public ResponseEntity<?> getUserEvents(@RequestParam(name = "start") String start, @RequestParam(name = "end") String end, @RequestHeader("Authorization") String authHeader) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));

        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            List<Event> userEvents = eventRepository.findByStartDateBetween(startDate,endDate);
            userEvents.removeIf(event -> event.getEventStatus().toString().equals("CREATED") || event.getEventStatus().toString().equals("REQUESTED") || event.getEventStatus().toString().equals("CHANGED"));
            List<Event> filteredEvents = userEvents.stream()
                    .filter(event -> event.getUserId().equals(calendarService.getUserId(authHeader)))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredEvents);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-requested-events")
    public ResponseEntity<?> getUserRequestedEvents(@RequestHeader("Authorization") String authHeader) {

        try {
            List<Event> reqEvents = eventRepository.findByEventStatus(EventStatus.REQUESTED);
            List<Event> userReqEvents = reqEvents.stream()
                    .filter(event -> event.getUserId().equals(calendarService.getUserId(authHeader)))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userReqEvents);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-requested-other")
    public ResponseEntity<?> getOtherRequestedEvents(@RequestHeader("Authorization") String authHeader) {

        try {
            List<Event> reqEvents = eventRepository.findByEventStatus(EventStatus.REQUESTED);
            reqEvents.removeIf(event -> event.getUserId().equals(calendarService.getUserId(authHeader)));
            return ResponseEntity.ok(reqEvents);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    ;

    @PutMapping("/edit-event/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable String id, @RequestBody Event newEvent) {
        try {
            calendarService.validateEvent(newEvent);
            return new ResponseEntity<>("Evento actualizado correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/req-change/{id}")
    public ResponseEntity<?> requestChange(@PathVariable String id) {
        try {
            calendarService.requestChange(id);
            return new ResponseEntity<>("Cambio solicitado correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/req-cancel/{id}")
    public ResponseEntity<?> requestCancel(@PathVariable String id) {
        try {
            calendarService.requestCancel(id);
            return new ResponseEntity<>("Cambio cancelado correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/req-accept/{id}")
    public ResponseEntity<?> requestChange(@RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        try {
            calendarService.requestChange(authHeader, id);
            return new ResponseEntity<>("Cambio aceptado correctamente, espere autorización del administrador", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/req-approve/{id}")
    public ResponseEntity<?> requestAccept(@PathVariable String id) {
        try {
            calendarService.requestAccept(id);
            return new ResponseEntity<>("Cambio aceptado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/req-change-cancel/{id}")
    public ResponseEntity<?> requestChangeCancel(@PathVariable String id) {
        try {
            calendarService.requestChangeCancel(id);
            return new ResponseEntity<>("Cambio cancelado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {

        try {
            Optional opEvent = eventRepository.findById(id);

            if (opEvent.isPresent()) {
                eventRepository.deleteById(id);
                return new ResponseEntity<>("Evento borrado correctamente.", HttpStatus.OK);
            }

        } catch (Exception e) {
            throw new AppException("Error al borrar evento.", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @GetMapping("/get-changed-events")
    public ResponseEntity<?> getChangedEvents(){
        List<Event> events = eventRepository.findByEventStatus(EventStatus.CHANGED);
        if (!events.isEmpty()) {
            return ResponseEntity.ok(events);
        } else {
            return null;
        }
    }
}
