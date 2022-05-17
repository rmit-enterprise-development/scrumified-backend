package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.PaginationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Service<T> {
    PaginationDto<List<T>> findAll(String key, Pageable pageable);

    T findOne(Long id);

    T create(T input);

    T update(Long id, T input);

    void delete(Long id);
}
