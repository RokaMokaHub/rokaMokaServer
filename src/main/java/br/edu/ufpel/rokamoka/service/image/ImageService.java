package br.edu.ufpel.rokamoka.service.image;

import br.edu.ufpel.rokamoka.core.Image;
import br.edu.ufpel.rokamoka.repository.ImageRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageService implements IIMageService {

    private final ImageRepository imageRepository;

    @Nullable
    @Override
    public Set<Image> upload(MultipartFile image) {
        if (image == null || image.getSize() == 0) {
            return null;
        }
        try {
            var imageSize = image.getSize();
            var imageBytes = image.getBytes();
            var savedImage = this.imageRepository.save(new Image(imageBytes, imageSize));
            return Set.of(savedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
