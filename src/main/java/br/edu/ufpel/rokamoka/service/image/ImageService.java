package br.edu.ufpel.rokamoka.service.image;

import br.edu.ufpel.rokamoka.core.Image;
import br.edu.ufpel.rokamoka.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@AllArgsConstructor
@Service
public class ImageService implements IIMageService {

    private final ImageRepository imageRepository;

    @Override
    public Set<Image> upload(MultipartFile image) {
        try {
            var imageSize = image.getSize();
            var imageBytes = image.getBytes();
            Image savedImage = this.imageRepository.save(new Image(imageBytes, imageSize));
            return Set.of(savedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
