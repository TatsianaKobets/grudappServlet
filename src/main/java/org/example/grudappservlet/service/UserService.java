package org.example.grudappservlet.service;

import java.sql.SQLException;
import java.util.Optional;
import org.example.grudappservlet.model.User;

public interface UserService extends Service<User, Long> {
 Optional<User> updateUser(User user) throws SQLException;
}
