package app.scrumifiedbackend.service.interface_service;

import java.util.List;

public interface Service<T> {
    List<T> findAll();

    T findOne(Long id);

    T create(T input);

    T update(Long id, T input);

    void delete(Long id);
}
