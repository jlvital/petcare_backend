package com.petcare.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String html = """
        <html>
          <head><title>PetCare Backend</title></head>
          <body style='text-align: center; font-family: sans-serif; background: #eef7ff; padding-top: 50px;'>
            <h2 style='color: #2c3e50;'>PetCare Backend en funcionamiento</h2>
            <img src='/info.png' alt='Estado del backend' style='max-width: 400px; margin-top: 20px;'/>
          </body>
        </html>
        """;
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
    }
}
