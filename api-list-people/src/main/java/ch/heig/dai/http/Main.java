package ch.heig.dai.http;

import io.javalin.*;

/**
 * @author Kevin Auberson, Adrian Rogner
 * @version 1.0
 * @file Main.java
 * @brief Main class for a simple RESTful API using the Javalin framework to manage persons.
 * @date 11.01.2024
 * <p>
 * The main class sets up a Javalin application with routes for handling HTTP requests related to persons.
 * It includes a root path ("/") with a welcome message and routes for listing, retrieving, adding, updating,
 * and deleting persons. The application runs on port 7777.
 * </p>
 */
public class Main {
    /**
     * Main method to start the Javalin application and define routes.
     *
     * @param args Command line arguments (unused in this application).
     */
    public static void main(String[] args) {
        // Create and start Javalin application on port 7777
        Javalin app = Javalin.create().start(7777);

        // Root path ("/") with a welcome message
        app.get("/", ctx -> ctx.result("Welcome to the Persons API!"));

        PersonController personController = new PersonController();

        // Define routes for handling various CRUD operations on persons
        app.get("/people", personController::getPersons);
        app.get("/person/{id}", personController::getPerson);
        app.post("/addPerson", personController::addPerson);
        app.put("/updatePerson/{id}", personController::updatePerson);
        app.delete("/deletePerson/{id}", personController::deletePerson);
    }
}