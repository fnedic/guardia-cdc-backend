package com.CDC.GuardiaBackend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CDC.GuardiaBackend.Entities.Notice;
import com.CDC.GuardiaBackend.Exceptions.AppException;
import com.CDC.GuardiaBackend.Repositories.NoticeRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createNotice(@RequestBody Notice notice) {

        try {
            Notice optionalNotice = noticeRepository.findById("55af7770-f99a-4dfc-ae69-a4957e21bc60")
                    .orElseThrow(() -> new RuntimeException("Aviso no encontrado"));
            optionalNotice.setTitle(notice.getTitle());
            noticeRepository.save(optionalNotice);
            return new ResponseEntity<>("Aviso publicado correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getNotice() {
        try {
            Notice optionalNotice = noticeRepository.findById("55af7770-f99a-4dfc-ae69-a4957e21bc60")
                   .orElseThrow(() -> new RuntimeException("Aviso no encontrado"));
            return new ResponseEntity<Notice>(optionalNotice, HttpStatus.OK);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
