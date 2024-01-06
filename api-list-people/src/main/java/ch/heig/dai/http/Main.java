package ch.heig.dai.http;

import io.javalin.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7777);
        app.get("/", ctx -> ctx.result("Welcome to the Person API!"));
        PersonController personController = new PersonController();
        app.get("/persons", personController::getPersons);
        app.get("persons/{id}", personController::getPerson);
        app.post("/addPerson", personController::addPerson);
        app.put("/persons/{id}", personController::updatePerson);
        app.delete("/delete-person/{id}", personController::deletePerson);
    }
}