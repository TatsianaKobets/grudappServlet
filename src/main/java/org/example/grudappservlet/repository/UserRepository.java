package org.example.grudappservlet.repository;

import java.sql.SQLException;
import java.util.Optional;
import org.example.grudappservlet.model.User;

public interface UserRepository<T, K> extends Repository<User, Long> {

  Optional<User> findByEmailAndPassword(String email, String password) throws SQLException;

}
