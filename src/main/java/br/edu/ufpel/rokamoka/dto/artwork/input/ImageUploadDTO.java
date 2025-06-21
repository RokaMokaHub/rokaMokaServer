package br.edu.ufpel.rokamoka.dto.artwork.input;

import org.springframework.web.multipart.MultipartFile;

public record ImageUploadDTO(MultipartFile image) {
}
