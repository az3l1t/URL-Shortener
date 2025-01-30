package net.azelit.url.controller;

import lombok.RequiredArgsConstructor;
import net.azelit.url.service.url.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/shortener")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String originalUrl)
            throws NoSuchAlgorithmException {
        String shortenedUrl = urlShortenerService.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortenedUrl);
    }
}
