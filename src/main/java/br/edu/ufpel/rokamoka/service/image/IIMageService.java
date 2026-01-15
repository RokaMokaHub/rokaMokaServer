package br.edu.ufpel.rokamoka.service.image;

import br.edu.ufpel.rokamoka.core.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IIMageService {
    Set<Image> upload(MultipartFile image);
}
