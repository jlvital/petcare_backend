package com.petcare.gallery;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder}")
    private String folder;

    @Override
    public List<String> getAllImageUrls() {
        List<String> urls = new ArrayList<>();

        try {
            Map result = cloudinary.search()
                    .expression("folder:" + folder)
                    .sortBy("created_at", "desc")
                    .maxResults(30)
                    .execute();

            List<Map> resources = (List<Map>) result.get("resources");

            for (Map resource : resources) {
                Object secureUrl = resource.get("secure_url");
                if (secureUrl != null) {
                    urls.add(secureUrl.toString());
                }
            }

        } catch (Exception e) {
            log.error("❌ Error al obtener imágenes de Cloudinary", e);
        }

        return urls;
    }
}
