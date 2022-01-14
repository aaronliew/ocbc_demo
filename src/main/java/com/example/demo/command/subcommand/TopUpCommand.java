package com.example.demo.command.subcommand;

import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.command.Username;
import com.example.demo.model.transaction.TopUpRequest;
import com.example.demo.model.transaction.TopUpResponse;
import com.example.demo.rest.TransactionApi;
import com.example.demo.util.CommandUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Paths;

import static picocli.CommandLine.Command;

@Command(name = "topup", description = "")
public class TopUpCommand implements Runnable {

    @CommandLine.Parameters(index = "0",paramLabel = "<topUpAmount>",
            description = "Top up amount")
    long amount;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;


    String BASE_URL = "http://localhost:8080/";

    @Override
    public void run() {
        LoginResponse loginResponse;
        try{
            loginResponse = readConfigFile();
            TopUpRequest topUpRequest = new TopUpRequest();
            topUpRequest.setUsername(loginResponse.getUsername());
            topUpRequest.setAmount(amount);
            TopUpResponse topUpResponse = TransactionApi.topUp(BASE_URL, topUpRequest);

            if (topUpResponse.getPayment() != null) {
                System.out.println("Transferred " + topUpResponse.getPayment().getAmount() +
                        " to " + topUpResponse.getPayment().getRecipientName());
            }
            System.out.println("Your balance is " + topUpResponse.getBalance());
            CommandUtil.printDebt(topUpResponse.getDebt());
        } catch (Exception e){
            throw new CommandLine.ExecutionException(spec.commandLine(), "Please login before you make payment");
        }

    }

    private LoginResponse readConfigFile() throws Exception{
        String lines = Files.readAllLines(Paths.get("./fileName.txt")).get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(lines, LoginResponse.class);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TopUpCommand()).execute(args);
        System.exit(exitCode);
    }
}
