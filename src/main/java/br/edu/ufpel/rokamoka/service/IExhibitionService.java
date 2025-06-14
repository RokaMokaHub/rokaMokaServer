package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;

import java.util.List;

public interface IExhibitionService {

    List<Exhibition> findAll();

    Exhibition findById(Long id) throws RokaMokaContentNotFoundException;

    Exhibition save(Long id, ExhibitionInputDTO exhibition) throws RokaMokaContentNotFoundException;

    Exhibition save(ExhibitionInputDTO exhibition) throws RokaMokaContentNotFoundException;

    void deleteById(Long id);
}
