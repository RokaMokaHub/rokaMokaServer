package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "imagem")
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "conteudo", nullable = false)
    private byte[] conteudo;

    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    // Construtores
    public Image() {
        this.dataUpload = LocalDateTime.now(); // Define a data de upload ao criar
    }

    public Image(byte[] conteudo, long tamanhoBytes) {
        this.conteudo = conteudo;
        this.tamanhoBytes = tamanhoBytes;
        this.dataUpload = LocalDateTime.now();
    }
}
