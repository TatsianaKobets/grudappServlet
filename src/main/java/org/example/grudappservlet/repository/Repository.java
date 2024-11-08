package org.example.grudappservlet.repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T, K> extends Serializable {
    Optional<T> findById(K k) throws SQLException;

    boolean deleteById(K k) throws SQLException;

    List<T> findAll() throws SQLException;

    Optional<T> save(T t) throws SQLException;

    void clearAll() throws SQLException;

}
