package org.example.grudappservlet.repository;

public class QueryConstants {

  public static final String GET_USERS = "SELECT * FROM users";
  public static final String GET_USER_BY_EMAIL_AND_PASSWORD = "SELECT id, name, email, password FROM users WHERE email=? AND password=?";
  public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
  public static final String SAVE_USER = "INSERT INTO users (name, email, password) VALUES (?, ?, ?) RETURNING id";
  public static final String UPDATE_USER_PROFILE = "UPDATE users SET name=?, email=?, password=? WHERE id=?";
  public static final String DELETE_USER = "DELETE FROM id WHERE email = ?";
  public static final String DELETE_ALL_USER = "DELETE FROM users";
 }
