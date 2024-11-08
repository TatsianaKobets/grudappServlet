package org.example.grudappservlet.servlet.dto.user.mapper;

import java.util.List;
import org.example.grudappservlet.model.User;
import org.example.grudappservlet.servlet.dto.user.EntityAllOutGoingDTO;
import org.example.grudappservlet.servlet.dto.user.EntityIncomingDTO;
import org.example.grudappservlet.servlet.dto.user.EntityOutGoingAndUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDtoMapper {

  UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

  User map(EntityIncomingDTO entityIncomingDTO);

  EntityOutGoingAndUpdateDTO map(User user);

  User map(EntityOutGoingAndUpdateDTO entityOutGoingAndUpdateDTO);

  default EntityAllOutGoingDTO mapListToDto(List<User> userList) {
    EntityAllOutGoingDTO entityAllOutGoingDTO = new EntityAllOutGoingDTO();
    entityAllOutGoingDTO.setUserList(userList);
    return entityAllOutGoingDTO;
  }
}