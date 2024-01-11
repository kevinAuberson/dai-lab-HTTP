package ch.heig.dai.http;

import io.javalin.http.Context;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class PersonController {
    public LinkedList<Person> persons;

    public PersonController(){
        persons = new LinkedList<>();
        persons.add(new Person("John", "Doe"));
        persons.add(new Person("Jane", "Doe"));
        persons.add(new Person("Jack", "Doe"));
    }
    public void getPersons(Context ctx) {

        ctx.json(persons);

    }
    public void getPerson(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (id >= 0 && id < persons.size()) {
            ctx.json(persons.get(id));
        } else {
            ctx.status(404).result("Person not found at index : " + id);
        }
    }
    public void addPerson(Context ctx) {/*
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");*/
        Person person = ctx.bodyAsClass(Person.class);

        if (person.getFirstName() != null && person.getLastName() != null) {
            persons.add(person);
            ctx.result("Person added successfully: " + person);
        } else {
            ctx.status(400).result("Invalid input. Both first name and last name are required.");
        }
    }
    public void updatePerson(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Person person = ctx.bodyAsClass(Person.class);
        if (id >= 0 && id < persons.size()) {
            persons.set(id,person);
            ctx.result("Person updated : " + person);
        } else {
            ctx.status(404).result("Person not found at index : " + id);
        }
    }
    public void deletePerson(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (id >= 0 && id < persons.size()) {
            ctx.result("Person deleted : " + persons.remove(id));
        } else {
            ctx.status(404).result("Person not found at index : " + id);
        }
    }
}
