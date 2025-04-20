package com.register.DB.Repository;

import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;

import com.register.DTO.Request.UserRegistrationRequest;

import jakarta.inject.Singleton;

/**
 * Repository class for managing user-related database operations.
 */
@Singleton
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        String sql = "INSERT INTO users (name, surname, email, username, password) VALUES (?, ?, ?, ?, ?)";

        Object[] args = { user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), password };
        int[] argTypes = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

        int rowsAffected = jdbcTemplate.update(sql, args, argTypes);

        if (rowsAffected > 0) {
            String getUserIdSql = "SELECT id FROM users WHERE username = ?";
            Integer userId = jdbcTemplate.queryForObject(getUserIdSql, new Object[] { user.getUsername() },
                    new int[] { Types.VARCHAR }, Integer.class);

            if (userId != null) {
                saveUserIpAddress(userIpAddress, userId);
            }
        }
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return boolean True if the user exists, false otherwise.
     */
    public boolean findUserByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Object[] args = { username };
        int[] argTypes = { Types.VARCHAR };
        Integer count = jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
        return count != null && count > 0;
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email The email to check.
     * @return boolean True if the user exists, false otherwise.
     */
    public boolean findUserByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Object[] args = { email };
        int[] argTypes = { Types.VARCHAR };
        Integer count = jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
        return count != null && count > 0;
    }

    // -------------------------------------------------------------------------
    // Validation Code Management
    // -------------------------------------------------------------------------

    /**
     * Saves the user's IP address in the database.
     *
     * @param userIpAddress The IP address to save.
     * @param userId        The ID of the user.
     */
    private void saveUserIpAddress(String userIpAddress, int userId) {
        String sql = "INSERT INTO user_ip_addresss (user_id, ip_address) VALUES (?, ?)";
        Object[] args = { userId, userIpAddress };
        int[] argTypes = { Types.INTEGER, Types.VARCHAR };
        jdbcTemplate.update(sql, args, argTypes);
    }

    /**
     * Saves a validation code for a user.
     *
     * @param email The user's email address.
     * @param code  The validation code to save.
     */
    public void saveUserValidationCode(String email, int code) {
        String sql = "UPDATE users SET validation_code = ? WHERE email = ?";
        Object[] args = { code, email };
        int[] argTypes = { Types.INTEGER, Types.VARCHAR };
        jdbcTemplate.update(sql, args, argTypes);
    }

    /**
     * Deletes the validation code for a user.
     *
     * @param email The user's email address.
     */
    public void deleteUserValidationCode(String email) {
        String sql = "UPDATE users SET validation_code = NULL WHERE email = ?";
        Object[] args = { email };
        int[] argTypes = { Types.VARCHAR };
        jdbcTemplate.update(sql, args, argTypes);
    }

    /**
     * Retrieves the number of validation attempts for a user.
     *
     * @param email The user's email address.
     * @return int The number of validation attempts.
     */
    public int getUserValidationCodeTryCount(String email) {
        String sql = "SELECT validation_try_count FROM users WHERE email = ?";
        Object[] args = { email };
        int[] argTypes = { Types.VARCHAR };
        return jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
    }

    /**
     * Updates the number of validation attempts for a user.
     *
     * @param email    The user's email address.
     * @param tryCount The number of attempts to save.
     */
    public int getUserValidationCode(String email) {
        String sql = "SELECT validation_code FROM users WHERE email = ?";
        Object[] args = { email };
        int[] argTypes = { Types.VARCHAR };
        return jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
    }

    /**
     * Updates the number of validation attempts for a user.
     *
     * @param email    The user's email address.
     * @param tryCount The number of attempts to save.
     */
    public void updateUserValidationTryCount(String email, int tryCount) {
        String sql = "UPDATE users SET validation_try_count = ? WHERE email = ?";
        Object[] args = { tryCount, email };
        int[] argTypes = { Types.INTEGER, Types.VARCHAR };
        jdbcTemplate.update(sql, args, argTypes);
    }

    /**
     * Retrieves the instance time of the validation code for a user.
     *
     * @param email The user's email address.
     * @return String The instance time of the validation code as a string (or null
     *         if not found).
     */
    public String getValidationCodeInstanceTime(String email) {
        String sql = "SELECT instance_time FROM users WHERE email = ?";
        Object[] args = { email };
        int[] argTypes = { Types.VARCHAR };

        try {
            return jdbcTemplate.queryForObject(sql, args, argTypes, String.class);
        } catch (Exception e) {
            // Handle cases where no result is found or other exceptions occur
            return null;
        }
    }
}