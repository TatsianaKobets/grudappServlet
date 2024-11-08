package org.example.grudappservlet.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Service<T, K> {

  Optional<T> save(T t) throws SQLException;

  Optional<T> findById(K k) throws SQLException;

  List<T> findAll() throws SQLException;

  boolean delete(K k) throws SQLException;

  Object getRepository();
}



