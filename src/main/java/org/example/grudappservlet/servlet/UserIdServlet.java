package org.example.grudappservlet.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.example.grudappservlet.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "UserIdServlet", value = "/userId/*")
public class UserIdServlet extends HttpServlet {

  private static final String APPLICATION_JSON = "application/json";
  private static final String UTF_8 = "UTF-8";
  private static final Logger LOGGER = LoggerFactory.getLogger(UserIdServlet.class);
  private ObjectMapper mapper = new ObjectMapper();
  private final transient ConnectionManager connectionManager;
  private final transient UserRepository<User, Long> userRepository;
  private transient Service<User, Long> service;

  public UserIdServlet() {
    this(new HikariCPDataSource());
  }

  public UserIdServlet(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
    this.userRepository = new UserRepositoryImpl(connectionManager);
    this.service = new UserServiceImpl(userRepository);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      String id = extractIdFromRequest(request);
      if (id == null) {
        sendBadRequest(response, "Invalid ID format");
        return;
      }
      processGetRequest(id, response);
    } catch (Exception e) {
      handleException(response, e, "Failed to process GET request");
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      String id = extractIdFromRequest(request);
      if (id == null) {
        sendBadRequest(response, "Invalid ID format");
        return;
      }
      processPutRequest(id, request, response);
    } catch (Exception e) {
      handleException(response, e, "Failed to process PUT request");
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      String pathInfo = request.getPathInfo();
      if (pathInfo != null && !pathInfo.isEmpty()) {
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length > 1) {
          Long entityID = Long.valueOf(pathParts[pathParts.length - 1]);

          service.delete(entityID);
          response.setContentType(APPLICATION_JSON);
          response.setCharacterEncoding(UTF_8);
          response.getWriter().write("Deleted User ID:" + entityID);
        }
      }
    } catch (Exception e) {
      handleException(response, e, "Failed to process DELETE request");
    }
  }

  private String extractIdFromRequest(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo == null || pathInfo.isEmpty()) {
      return null;
    }
    String[] pathParts = pathInfo.split("/");
    return pathParts.length > 1 ? String.valueOf(Long.valueOf(pathParts[pathParts.length - 1]))
        : null;
  }

  private void processGetRequest(String id, HttpServletResponse response)
      throws IOException, SQLException {
    setResponseDefaults(response);
    Long longId;
    try {
      longId = Long.valueOf(id);
    } catch (IllegalArgumentException e) {
      sendBadRequest(response, "Invalid ID format");
      return;
    }

    Optional<User> userOptional = service.findById(longId);
    if (!userOptional.isPresent()) {
      sendNotFound(response);
      return;
    }

    User user = userOptional.get();
    String jsonString = mapper.writeValueAsString(user);
    response.getWriter().write(jsonString);
  }

  private void processPutRequest(String id, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    setResponseDefaults(response);
    Long longId;
    try {
      longId = Long.valueOf(id);
    } catch (IllegalArgumentException e) {
      sendBadRequest(response, "Invalid ID format");
      return;
    }

    StringBuilder sb = getStringFromRequest(request);
    User user;
    try {
      user = mapper.readValue(sb.toString(), User.class);
    } catch (JsonProcessingException e) {
      sendBadRequest(response, "Invalid JSON format");
      return;
    }

    user.setId(longId);

    try {
      service.save(user);
      response.getWriter().write("User updated successfully");
    } catch (SQLException e) {
      LOGGER.error("Failed to save User", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void sendBadRequest(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.getWriter().write(message);
  }

  private void sendNotFound(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  private void setResponseDefaults(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(APPLICATION_JSON);
    response.setCharacterEncoding(UTF_8);
  }

  private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    try (BufferedReader reader = request.getReader()) {
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    }
    return sb;
  }

  private void handleException(HttpServletResponse response, Exception e, String logMessage)
      throws IOException {
    LOGGER.error(logMessage, e);
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.setContentType(APPLICATION_JSON);
    response.setCharacterEncoding(UTF_8);
    response.getWriter().write("An internal server error occurred.");
  }

  protected void setService(Service<User, Long> service) {
    this.service = service;
  }

  protected void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }
}
