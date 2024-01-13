package ch.heig.dai.http;

import io.javalin.http.Context;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author Kevin Auberson, Adrian Rogner
 * @version 1.0
 * @file PersonController.java
 * @brief Controller class for managing persons through HTTP requests.
 * @date 11.01.2024
 * <p>
 * This class handles CRUD operations for persons, including listing all persons,
 * retrieving a specific person, adding a new person, updating an existing person,
 * and deleting a person.
 * </p>
 */
public class PersonController {

    /**
     * List of persons managed by the controller.
     */
    public LinkedList<Person> persons;
    private static final Logger LOG = Logger.getLogger(PersonController.class.getName());

    /**
     * Constructor to initialize the list of persons with sample data.
     */
    public PersonController() {
        persons = new LinkedList<>();
        persons.add(new Person("John", "Doe"));
        persons.add(new Person("Jane", "Doe"));
        persons.add(new Person("Jack", "Doe"));
    }

    /**
     * Handles a GET request to retrieve the list of all persons.
     *
     * @param ctx The Javalin context object representing the HTTP request and response.
     */
    public void getPersons(Context ctx) {
        ctx.json(persons);
        LOG.info("GET " + persons);
    }

    /**
     * Handles a GET request to retrieve a specific person by ID.
     *
     * @param ctx The Javalin context object representing the HTTP request and response.
     */
    public void getPerson(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (id >= 0 && id < persons.size()) {
            ctx.json(persons.get(id));
            LOG.info("GET " + persons.get(id).toString());
        } else {
            ctx.status(404).result("Person not found at index : " + id);
        }
    }

    /**
     * Handles a POST request to add a new person.
     *
     * @param ctx The Javalin context object representing the HTTP request and response.
     */
    public void addPerson(Context ctx) {
        Person person = ctx.bodyAsClass(Person.class);
        if (person.getFirstName() != null && person.getLastName() != null) {
            persons.add(person);
            ctx.result("Person added successfully: " + person);
            LOG.info("POST " + person);
        } else {
            ctx.status(400).result("Invalid input. Both first name and last name are required.");
        }
    }

    /**
     * Handles a PUT request to update an existing person by ID.
     *
     * @param ctx The Javalin context object representing the HTTP request and response.
     */
    public void updatePerson(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Person person = ctx.bodyAsClass(Person.class);
        if (id >= 0 && id < persons.size()) {
            persons.set(id, person);
            ctx.result("Person updated : " + person);
            LOG.info("PUT " + person);
        } else {
            ctx.status(404).result("Person not found at index : " + id);
        }
    }

    /**
     * Handles a DELETE request to delete a person by ID.
     *
     * @param ctx The Javalin context object representing the HTTP request and response.
     */
    public void deletePerson(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (id >= 0 && id < persons.size()) {
            LOG.info("DELETE  " + persons.get(id).toString());
            ctx.result("Person deleted : " + persons.remove(id));
        } else {
            ctx.status(404).result("Person not found at index : " + id);
        }
    }
}