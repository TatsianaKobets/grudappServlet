package org.example.grudappservlet.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import org.example.grudappservlet.db.ConnectionManager;
import org.example.grudappservlet.db.HikariCPDataSource;
import org.example.grudappservlet.model.User;
import org.example.grudappservlet.repository.UserRepository;
import org.example.grudappservlet.repository.impl.UserRepositoryImpl;
import org.example.grudappservlet.service.Service;
import org.example.grudappservlet.service.UserService;
import org.example.grudappservlet.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "UpdateUserServlet", value = "/updateUser")
public class UpdateUserServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserIdServlet.class);
  private ObjectMapper mapper = new ObjectMapper();
  private final ConnectionManager connectionManager;
  private final UserRepository<User, Long> userRepository;
  private transient Service<User, Long> service;

  private final UserService userService;

  public UpdateUserServlet() {
    this.connectionManager = new HikariCPDataSource();
    this.userRepository = new UserRepositoryImpl(this.connectionManager);
    this.service = new UserServiceImpl(userRepository);
    this.userService = new UserServiceImpl(userRepository);
  }
  public UpdateUserServlet(ConnectionManager connectionManager,
      UserRepository<User, Long> userRepository, UserService userService) {
    this.connectionManager = connectionManager;
    this.userRepository = userRepository;
    this.userService = userService;
  }

  public UpdateUserServlet(UserService userService) {
    this.userService = userService;
    this.connectionManager = new HikariCPDataSource();
    this.userRepository = new UserRepositoryImpl(this.connectionManager);
    this.service = new UserServiceImpl(userRepository);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    String pathInfo = request.getPathInfo();
      try {
        String json = stringJson(request).toString();
        User user = mapper.readValue(json, User.class);

        // Проверим, что пользователь с таким ID существует
        Optional<User> existingUserOpt = service.findById(user.getId());
        if (existingUserOpt.isPresent()) {
          // Если существует, обновим его данные
          Optional<User> updatedUserOpt = userService.updateUser(user);
          if (updatedUserOpt.isPresent()) {
            sendSuccessResponse(response,
                "User updated successfully, ID: " + updatedUserOpt.get().getId(),
                HttpServletResponse.SC_OK);
          } else {
            writeResponse(response, "Failed to update user.", HttpServletResponse.SC_BAD_REQUEST,
                "text/plain");
          }
        } else {
          writeResponse(response, "User not found.", HttpServletResponse.SC_NOT_FOUND,
              "text/plain");
        }

      } catch (SQLException e) {
        handleException(response, e, "Failed to update user");
      } catch (Exception e) {
        handleException(response, e, "Failed to process update request");
      }
  }

  private StringBuilder stringJson(HttpServletRequest request) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    try (BufferedReader reader = request.getReader()) {
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    }
    return sb;
  }

  private void sendSuccessResponse(HttpServletResponse response, String message, int statusCode)
      throws
      IOException {
    writeResponse(response, message, statusCode, "application/json");
  }

  void handleException(HttpServletResponse response, Exception e, String logMessage) {
    LOGGER.error(logMessage, e);
    try {
      writeResponse(response, "An internal server error occurred.",
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "text/plain");
    } catch (IOException ioException) {
      LOGGER.error("Failed to send error response.", ioException);
    }
  }

  private void writeResponse(HttpServletResponse response, String message, int statusCode,
      String contentType) throws IOException {
    response.setContentType(contentType);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(statusCode);
    response.getWriter().write(message);
  }

  protected void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

}
