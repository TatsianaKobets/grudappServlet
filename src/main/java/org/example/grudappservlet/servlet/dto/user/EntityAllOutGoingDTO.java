package org.example.grudappservlet.servlet.dto.user;

import java.util.List;
import org.example.grudappservlet.model.User;

public class EntityAllOutGoingDTO {

    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
