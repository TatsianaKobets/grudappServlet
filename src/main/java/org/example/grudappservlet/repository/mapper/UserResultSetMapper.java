package org.example.grudappservlet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.grudappservlet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResultSetMapper {

  UserResultSetMapper INSTANCE = Mappers.getMapper(UserResultSetMapper.class);

  default User map(ResultSet resultSet) throws SQLException {
    User user = new User();
    user.setId(resultSet.getLong("id"));
    user.setName(resultSet.getString("name"));
    user.setEmail(resultSet.getString("email"));
    user.setPassword(resultSet.getString("password"));
    return user;
  }
}
