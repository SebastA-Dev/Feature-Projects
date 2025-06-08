package com.register.DB.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUsername());
            statement.setString(5, password);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                saveUserIpAddress(userIpAddress, user.getUsername());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while saving user: " + e.getMessage(), e);
        }
    }

    /**
     * Saves the user's IP address in the database.
     *
     * @param userIpAddress The IP address to save.
     * @param username      The username of the user.
     */
    private void saveUserIpAddress(String userIpAddress, String username) {
        String sql = "INSERT INTO UserIPAddress (userId, ipAddress, DateTime) " +
                "SELECT idUser, ?, NOW() FROM User WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userIpAddress);
            statement.setString(2, username);

            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error while saving user IP address: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return boolean True if the user exists, false otherwise.
     */
    public boolean findUserByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while checking username: " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email The email to check.
     * @return boolean True if the user exists, false otherwise.
     */
    public boolean findUserByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while checking email: " + e.getMessage(), e);
        }

        return false;
    }
}