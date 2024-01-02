package com.CDC.GuardiaBackend.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CDC.GuardiaBackend.Entities.Video;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.VideoRepository;
import com.CDC.GuardiaBackend.Services.VideoService;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    VideoRepository videoRepository;
    @Autowired
    ProtocolController protocolController;
    @Autowired
    VideoService videoService;

    @GetMapping("/get")
    public ResponseEntity<List<Video>> getAll() {
        List<Video> videosList = videoRepository.findAll();
        if (!videosList.isEmpty()) {
            return new ResponseEntity<>(videosList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody Video video) throws AppException{
        try {
            videoService.create(video.getTitle(), video.getLink(), video.getDate(), video.getVideoGroup());
            return new ResponseEntity<>("Video publicado correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el id: " + id));
        return ResponseEntity.status(HttpStatus.OK).body(video);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVideo(@PathVariable String id, @RequestBody Video updatedVideo) {

        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existingVideo.setTitle(updatedVideo.getTitle());
        existingVideo.setVideoGroup(updatedVideo.getVideoGroup());
        existingVideo.setLink(updatedVideo.getLink());

        videoRepository.save(existingVideo);

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable String id) {
        try {
            Optional<Video> optionalVideo = videoRepository.findById(id);
            if (optionalVideo.isPresent()) {
                videoRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar solicitud en el controlador");
        }
    }


}
