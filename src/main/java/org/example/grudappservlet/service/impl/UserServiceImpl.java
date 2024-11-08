package org.example.grudappservlet.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.example.grudappservlet.model.User;
import org.example.grudappservlet.repository.UserRepository;
import org.example.grudappservlet.service.UserService;

public class UserServiceImpl implements UserService {

  private final UserRepository<User, Long> userRepository;

  public UserServiceImpl(UserRepository<User, Long> userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> save(User user) throws SQLException {
    userRepository.save(user);
    return Optional.of(user);
  }

  @Override
  public Optional<User> findById(Long id) throws SQLException {
    return userRepository.findById(id);
  }

  @Override
  public List<User> findAll() throws SQLException {
    return userRepository.findAll();
  }

  @Override
  public boolean delete(Long aLong) throws SQLException {
    return userRepository.deleteById(aLong);
  }

  @Override
  public UserRepository<User, Long> getRepository() {
    return this.userRepository;
  }

 @Override
 public Optional<User> updateUser(User user) throws SQLException {
   return userRepository.save(user);
 }
}


