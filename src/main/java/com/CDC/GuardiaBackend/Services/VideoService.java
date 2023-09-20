package com.CDC.GuardiaBackend.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.CDC.GuardiaBackend.Entities.Video;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Repositories.VideoRepository;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    public void create(String title, String link, Date date, String group) {

        try {
            Optional<Video> optional = videoRepository.findByLink(link);
            if (optional.isPresent()) {
                throw new AppException("El video (link) ya se encuentra registrado!", HttpStatus.BAD_REQUEST);
            }
            Video video = new Video();
            video.setLink(link);
            video.setTitle(title);
            video.setDate(date);
            video.setVideoGroup(group);
            videoRepository.save(video);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
