package org.example.grudappservlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

  private Long id;
  private String name;
  private String email;
  private String password;
  private List<Habit> habits;
  private List<Log> logs;
  private boolean blocked;
  private Role role;

  public Map<Long, User> users = new HashMap<>();

  public User(long id) {
    this.id = id;
  }

  public User() {
  }

  public User(long id, String name, String email, String password,  Role role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
    this.blocked = false;
    this.role = role;
  }

  public User(long id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
  }

  public User(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
  }

  public User(String name, String email, String password, Role role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public List<Habit> getHabits() {
    return habits;
  }
  public List<Log> getLogs() {
    return logs;
  }
  public boolean isBlocked() {
    return blocked;
  }
  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }
  public Role getRole() {
    return role;
  }
  public void setRole(Role role) {
    this.role = role;
  }

  public Map<Long, User> getUsers() {
    return users;
  }
  public void setUsers(Map<Long, User> users) {
    this.users = users;
  }
}

