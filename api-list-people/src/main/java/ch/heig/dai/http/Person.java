package ch.heig.dai.http;

/**
 * @author Kevin Auberson, Adrian Rogner
 * @version 1.0
 * @file Person.java
 * @brief Represents a person with first and last names.
 * @date 11.01.2024
 * <p>
 * This class provides a simple model for a person with methods to retrieve
 * the first name, last name, and a string representation of the person.
 * </p>
 */
public class Person {
    private String firstName = "";
    private String lastName = "";

    /**
     * Constructor to create a person with specified first and last names.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the first name of the person.
     *
     * @return The first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the person.
     *
     * @return The last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns a string representation of the person.
     *
     * @return A string representation of the person, formatted as "firstName lastName".
     */
    public String toString() {
        return firstName + " " + lastName;
    }
}
