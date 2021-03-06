package com.example.demo.command.subcommand;

import com.example.demo.exception.ApiException;
import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.command.Username;
import com.example.demo.rest.LoginApi;
import com.example.demo.util.CommandUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import picocli.CommandLine;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
        try {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            LoginResponse loginResponse = LoginApi.login(BASE_URL, loginRequest);
            writeConfigFile(loginResponse);
            System.out.println("Hello, " + loginResponse.getUsername());
            System.out.println("Your balance is " + loginResponse.getBalance());
            CommandUtil.printDebt(loginResponse.getDebt());
        } catch (ApiException e) {
            System.out.println(e.getErrorResponse().getMessage());
        } catch (Exception e) {
            throw new CommandLine.ExecutionException(spec.commandLine(), "Unable to write config file");
        }
    }

    private static void writeConfigFile(LoginResponse loginResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(loginResponse);
        Files.write(Paths.get("./credential.txt"), jsonStr.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new LoginCommand()).execute(args);
        System.exit(exitCode);
    }
}
