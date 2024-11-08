package org.example.grudappservlet.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.example.grudappservlet.db.ConnectionManager;
import org.example.grudappservlet.db.HikariCPDataSource;
import org.example.grudappservlet.model.User;
import org.example.grudappservlet.repository.UserRepository;
import org.example.grudappservlet.repository.impl.UserRepositoryImpl;
import org.example.grudappservlet.service.Service;
import org.example.grudappservlet.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "UserServlet", value = "/userServlet")
public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserIdServlet.class);
    private ObjectMapper mapper = new ObjectMapper();
    private final ConnectionManager connectionManager;
    private final UserRepository<User, Long> userRepository;
    private transient Service<User, Long> service;

    public UserServlet() {
        this.connectionManager = new HikariCPDataSource();
        this.userRepository = new UserRepositoryImpl(this.connectionManager);
        this.service = new UserServiceImpl(userRepository);
    }



    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<User> userList = service.findAll();
            String jsonString = mapper.writeValueAsString(userList);
            sendSuccessResponse(response, "GetAll User:" + jsonString, HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            handleException(response, e, "Failed to fetch all userList");
        } catch (Exception e) {
            handleException(response, e, "Failed to process GET request");
        }
    }

    /* @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
      try {
            String json = stringJson(request).toString();
            User user = mapper.readValue(json, User.class);
            service.save(user);
            sendSuccessResponse(response, "Added User ID:" +
                    user.getId(), HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            handleException(response, e, "Failed to save the User");
        } catch (Exception e) {
            handleException(response, e, "Failed to process POST request");
        }
    }*/
        /* String pathInfo = request.getPathInfo();

        try {
            if ("/register".equals(pathInfo)) {
                // Логика для регистрации пользователя
                String json = stringJson(request).toString();
                User user = mapper.readValue(json, User.class);
                service.save(user);
                sendSuccessResponse(response, "Added User ID: " + user.getId(), HttpServletResponse.SC_CREATED);
            } else if ("/login".equals(pathInfo)) {
                // Логика для авторизации пользователя
                String json = stringJson(request).toString();
                User user = mapper.readValue(json, User.class);

                Optional<User> authenticatedUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

                if (authenticatedUser.isPresent()) {
                    sendSuccessResponse(response, "Authenticated User ID: " + authenticatedUser.get().getId(),
                        HttpServletResponse.SC_OK);
                } else {
                    writeResponse(response, "Invalid email or password.", HttpServletResponse.SC_UNAUTHORIZED, "text/plain");
                }
            } else {
                // Обработка неверного пути
                writeResponse(response, "Invalid action.", HttpServletResponse.SC_BAD_REQUEST, "text/plain");
            }
        } catch (SQLException e) {
            handleException(response, e, "Database error occurred");
        } catch (Exception e) {
            handleException(response, e, "Failed to process POST request");
        }
    }*/


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

    private void sendSuccessResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
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

    protected void setService(Service<User, Long> service) {
        this.service = service;
    }
}
