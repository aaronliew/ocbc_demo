package com.example.demo.command.subcommand;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.rest.LoginApi;
import picocli.CommandLine;

import static picocli.CommandLine.Command;

@Command(name = "login", description = "Login with username")
public class LoginCommand implements Runnable {

    @CommandLine.Parameters(arity = "1",paramLabel = "<username>",
            description = "username")
    String username;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    String BASE_URL = "http://localhost:8080/";

    @Override
    public void run() {
        // The business logic of the command goes here...
        // In this case, code for generation of ASCII art graphics
        // (omitted for the sake of brevity).
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        LoginResponse loginResponse = LoginApi.login(BASE_URL, loginRequest);
        System.out.println(loginResponse.getMessage());
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new LoginCommand()).execute(args);
        System.exit(exitCode);
    }
}
