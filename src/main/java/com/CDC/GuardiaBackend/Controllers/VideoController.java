package com.CDC.GuardiaBackend.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Entities.Video;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Exceptions.MyException;
import com.CDC.GuardiaBackend.Repositories.VideoRepository;
import com.CDC.GuardiaBackend.Services.VideoService;

@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/delete/{id}")
    public String deleteVideo(@PathVariable String id) throws MyException {
        try {
            Optional<Video> optionalProtocol = videoRepository.findById(id);
            if (optionalProtocol.isPresent()) {
                videoRepository.deleteById(id);
                return null;
            } else {
                throw new MyException("Video no encontrado");
            }
        } catch (Exception e) {
            throw new MyException("Error al procesar solicitud en el controlador!");
        }
    }

}
