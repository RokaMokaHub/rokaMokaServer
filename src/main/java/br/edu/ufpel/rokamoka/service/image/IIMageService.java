package br.edu.ufpel.rokamoka.service.image;

import br.edu.ufpel.rokamoka.core.Image;
import io.micrometer.common.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IIMageService {

    @Nullable
    Set<Image> upload(MultipartFile image);
}
