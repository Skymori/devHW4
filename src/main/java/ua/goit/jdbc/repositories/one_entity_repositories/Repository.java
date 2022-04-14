package ua.goit.jdbc.repositories.one_entity_repositories;

import java.util.List;

public interface Repository<T> {

    T findById(int id);

    T create(T entity);

    T update(T locationDAO);

    T deleteById(int id);

    T deleteByObject(T object);

    List<T> findAll();

    List<Integer> getListOfValidIndexes();
}
