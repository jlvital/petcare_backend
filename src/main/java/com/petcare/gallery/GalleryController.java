package com.petcare.gallery;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/galeria")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    /**
     * Este endpoint devuelve todas las imágenes subidas por los clientes a Cloudinary.
     * No se necesita autenticación ya que solo devuelve URLs públicas.
     */
    @GetMapping
    public List<String> getGalleryImages() {
        return galleryService.getAllImageUrls();
    }
}