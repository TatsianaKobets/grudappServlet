package org.example.grudappservlet.repository.impl;

import static org.example.grudappservlet.repository.QueryConstants.DELETE_ALL_USER;
import static org.example.grudappservlet.repository.QueryConstants.DELETE_USER;
import static org.example.grudappservlet.repository.QueryConstants.GET_USERS;
import static org.example.grudappservlet.repository.QueryConstants.GET_USER_BY_EMAIL_AND_PASSWORD;
import static org.example.grudappservlet.repository.QueryConstants.GET_USER_BY_ID;
import static org.example.grudappservlet.repository.QueryConstants.SAVE_USER;
import static org.example.grudappservlet.repository.QueryConstants.UPDATE_USER_PROFILE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.grudappservlet.db.ConnectionManager;
import org.example.grudappservlet.model.User;
import org.example.grudappservlet.repository.UserRepository;
import org.example.grudappservlet.repository.mapper.UserResultSetMapper;

public class UserRepositoryImpl implements UserRepository<User, Long> {

  private final ConnectionManager connectionManager;

  public UserRepositoryImpl(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  @Override
  public Optional<User> findById(Long id) throws SQLException {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            GET_USER_BY_ID)) {
      preparedStatement.setObject(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(createUserFromResultSet(resultSet));
        }
        return Optional.empty();
      }
    }
  }

  private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
    return UserResultSetMapper.INSTANCE.map(resultSet);
  }

  @Override
  public List<User> findAll() throws SQLException {
    List<User> userList = new ArrayList<>();
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        userList.add(createUserFromResultSet(resultSet));
      }
    }
    return userList;
  }

  @Override
  public boolean deleteById(Long id) throws SQLException {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            DELETE_USER)) {
      preparedStatement.setObject(1, id);
      return preparedStatement.executeUpdate() > 0;
    }
  }

  @Override
  public Optional<User> save(User user) throws SQLException {
    try (Connection connection = connectionManager.getConnection()) {
      connection.setAutoCommit(false);
      try {
        Optional<User> result;
        if (user.getId() != null) {
          result = updateUser(user, connection);
        } else {
          result = insertUser(user, connection);
        }
        connection.commit();
        return result;
      } catch (SQLException e) {
        connection.rollback();
        throw e;
      }
    }
  }

  private Optional<User> updateUser(User user, Connection connection) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        UPDATE_USER_PROFILE)) {
      preparedStatement.setString(1, user.getName());
      preparedStatement.setString(2, user.getEmail());
      preparedStatement.setString(3, user.getPassword());
      preparedStatement.setObject(4, user.getId());
      preparedStatement.executeUpdate();
      return Optional.of(user);
    }
  }

  private Optional<User> insertUser(User user, Connection connection) throws SQLException {
    try (PreparedStatement preparedStatementUser = connection.prepareStatement(
        SAVE_USER,
        Statement.RETURN_GENERATED_KEYS)) {
      preparedStatementUser.setString(1, user.getName());
      preparedStatementUser.setString(2, user.getEmail());
      preparedStatementUser.setString(3, user.getPassword());
      preparedStatementUser.executeUpdate();

      try (ResultSet generatedKeys = preparedStatementUser.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          user.setId(generatedKeys.getLong(1));
        }
      }
      return Optional.of(user);
    }
  }

  @Override
  public void clearAll() throws SQLException {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement deleteUsers = connection.prepareStatement(DELETE_ALL_USER)) {
      deleteUsers.executeUpdate();
    }
  }

  @Override
  public Optional<User> findByEmailAndPassword(String email, String password) throws SQLException {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            GET_USER_BY_EMAIL_AND_PASSWORD)) {
      preparedStatement.setString(1, email);
      preparedStatement.setString(2, password);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(createUserFromResultSet(resultSet));
        }
        return Optional.empty();
      }
    }
  }
}