package com.register.DB.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;
import java.util.function.Function;

import com.register.DTO.Request.UserRegistrationRequest;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javax.sql.DataSource;

/**
 * Repository class for managing user-related database operations.
 */
@Singleton
public class UserRepository {

    @Inject
    private DataSource dataSource;

    // -------------------------------------------------------------------------
    // User Management
    // -------------------------------------------------------------------------

    /**
     * Saves a user in the database.
     *
     * @param user          The user object to be saved.
     * @param password      The hashed password of the user.
     * @param userIpAddress The IP address of the user.
     */
    public void saveUser(UserRegistrationRequest user, String password, String userIpAddress) {
        String sql = "INSERT INTO User (name, surname, email, username, password) VALUES (?, ?, ?, ?, ?)";
        setValuesIntoDB(sql, user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), password);
        saveUserIpAddress(userIpAddress, user.getUsername());
    }

    /**
     * Saves the user's IP address in the database.
     *
     * @param userIpAddress The IP address to save.
     * @param username      The username of the user.
     */
    private void saveUserIpAddress(String userIpAddress, String username) {
        Boolean userIpExistence = userIpAddressExistence(username, null);

        if (userIpExistence) {
            return;
        }

        String sql = "INSERT INTO UserIPAddress (userId, ipAddress, DateTime) " +
                "SELECT idUser, ?, NOW() FROM User WHERE username = ?";
        setValuesIntoDB(sql, userIpAddress, username);
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return boolean True if the user exists, false otherwise.
     */
    public boolean findUserByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ?";
        return getValuesFromDB(sql, rs -> {
            try {
                return rs.next() && rs.getInt(1) > 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, username);
    }

    /**
     * checks if a user exist by username and the corresponding password
     * 
     * @param username
     * @param password
     * @return
     */
    public boolean findUserByUsername(String username, String password) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ? AND password = ?";
        return getValuesFromDB(sql, rs -> {
            try {
                return rs.next() && rs.getInt(1) > 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, username, password);
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email The email to check.
     * @return boolean True if the user exists, false otherwise.
     */
    public boolean findUserByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ?";
        return getValuesFromDB(sql, rs -> {
            try {
                return rs.next() && rs.getInt(1) > 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, email);
    }

    /**
     * Authenticates the user and creates a session if valid.
     *
     * @param username     The username (optional)
     * @param email        The email (optional)
     * @param creationDate The timestamp when the session is created
     * @param expiresDate  The timestamp when the session should expire
     */
    public void saveUserSession(String username, String email,
            LocalDateTime creationDate, LocalDateTime expiresDate, LocalDateTime refreshExpiresDate) {

        String userUUID = generateRandomUUID();
        String userRefreshUUID = generateRandomUUID();
        int userId = findUserId(username, email);
        int userIpAddressId = getUserIdIpAddress(username, email);

        if (userId == -1 || userIpAddressId == -1) {
            throw new RuntimeException("Cannot create session: user or IP address not found.");
        }

        String sql = "INSERT INTO Session (sessionUUID, refreshUUID, userId, creationDate, expiresDate, refreshExpiresDate, ipAddressID) "
                +
                "VALUES (?, ?, ?, ?, ?)";

        setValuesIntoDB(sql, userUUID, userRefreshUUID, userId, creationDate, expiresDate, refreshExpiresDate,
                userIpAddressId);
    }

    /**
     * Return the UUID from the desired user.
     *
     * @param username The user's username
     * @param email    The user's email
     * @return A dictionary with keys "UUID" and "RefreshUUID", or null if not found
     */
    public Dictionary<String, String> getUserUUID(String username, String email) {
        int userId = findUserId(username, email);
        String sql = "SELECT sessionUUID, RefreshUUID FROM Session WHERE userId = ?";

        return getValuesFromDB(sql, rs -> {
            try {
                if (rs.next()) {
                    String sessionUUID = rs.getString("sessionUUID");
                    String refreshUUID = rs.getString("RefreshUUID");

                    if (sessionUUID != null && refreshUUID != null) {
                        Dictionary<String, String> result = new Hashtable<>();
                        result.put("UUID", sessionUUID);
                        result.put("RefreshUUID", refreshUUID);
                        return result;
                    }
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving UUIDs: " + e.getMessage(), e);
            }
        }, userId);
    }

    // -------------------------------------------------------------------------
    // Shared methods for DB interaction
    // -------------------------------------------------------------------------

    /**
     * General-purpose method for INSERT/UPDATE/DELETE.
     */
    private void setValuesIntoDB(String sql, Object... params) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error during DB write operation: " + e.getMessage(), e);
        }
    }

    /**
     * General-purpose method for SELECT operations.
     * Executes the query and allows a custom ResultSet handler.
     *
     * @param sql     The SQL query
     * @param handler Function that processes the ResultSet
     * @param params  Query parameters
     * @return T The result from the handler
     */
    private <T> T getValuesFromDB(String sql, Function<ResultSet, T> handler, Object... params) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return handler.apply(resultSet);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error during DB read operation: " + e.getMessage(), e);
        }
    }

    /**
     * Checks credentials and returns the user ID if correct.
     */
    private Integer findUserId(String username, String email) {
        String sql = "SELECT idUser FROM User WHERE " +
                "((? IS NOT NULL AND username = ?) OR (? IS NOT NULL AND email = ?))";

        return getValuesFromDB(sql, rs -> {
            try {
                if (rs.next()) {
                    return rs.getInt("idUser");
                } else {
                    return -1;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving user ID: " + e.getMessage(), e);
            }
        }, username, username, email, email);
    }

    /**
     * Checks whether a UserIPAddress entry exists for a given user.
     *
     * @param username The username of the user
     * @param email    The email of the user
     * @return true if the user has at least one IP address logged, false otherwise
     */
    private Boolean userIpAddressExistence(String username, String email) {
        String sql = "SELECT COUNT(*) FROM UserIPAddress WHERE userId = ?";

        Integer userId = findUserId(username, email);
        if (userId == null) {
            return false;
        }

        return getValuesFromDB(sql, rs -> {
            try {
                return rs.next() && rs.getInt(1) > 0;
            } catch (Exception e) {
                throw new RuntimeException("Error checking UserIPAddress existence: " + e.getMessage(), e);
            }
        }, userId);
    }

    private int getUserIdIpAddress(String username, String email) {
        String sql = "SELECT idUserIPAddress FROM UserIPAddress WHERE userId = ?";
        Integer userId = findUserId(username, email);
        if (userId == null) {
            return -1;
        }

        return getValuesFromDB(sql, rs -> {
            try {
                if (rs.next()) {
                    return rs.getInt("idUserIPAddress");
                } else {
                    return -1;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error checking UserIPAddress existence: " + e.getMessage(), e);
            }
        }, userId);
    }

    /**
     * Generates a random UUID string.
     *
     * @return A randomly generated UUID as a string.
     */
    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

}
