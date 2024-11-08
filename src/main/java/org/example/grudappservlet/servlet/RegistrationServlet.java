package org.example.grudappservlet.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import org.example.grudappservlet.db.ConnectionManager;
import org.example.grudappservlet.db.HikariCPDataSource;
import org.example.grudappservlet.model.User;
import org.example.grudappservlet.repository.impl.UserRepositoryImpl;
import org.example.grudappservlet.service.Service;
import org.example.grudappservlet.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "RegistrationServlet", value = "/register")
public class RegistrationServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServlet.class);
  private ObjectMapper mapper = new ObjectMapper();
  private final Service<User, Long> service;

  public RegistrationServlet() {
    this.service = new UserServiceImpl(new UserRepositoryImpl(new HikariCPDataSource()));
  }
public RegistrationServlet(ConnectionManager connectionManager) {
  this.service = new UserServiceImpl(new UserRepositoryImpl(connectionManager));

}
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      String json = stringJson(request).toString();
      User user = mapper.readValue(json, User.class);
      service.save(user);
      sendSuccessResponse(response, "Added User ID: " + user.getId(),
          HttpServletResponse.SC_CREATED);
    } catch (SQLException e) {
      handleException(response, e, "Failed to save the UserEntity");
    } catch (Exception e) {
      handleException(response, e, "Failed to process POST request");
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
