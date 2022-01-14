package com.example.demo.command.subcommand;

import com.example.demo.exception.ApiException;
import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.command.Username;
import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.PaymentResponse;
import com.example.demo.rest.LoginApi;
import com.example.demo.rest.TransactionApi;
import com.example.demo.util.CommandUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static picocli.CommandLine.Command;

@Command(name = "pay", description = "")
public class PaymentCommand implements Runnable {

    @CommandLine.Parameters(index = "0",paramLabel = "<recipientName>",
            description = "Recipient name")
    String recipientName;

    @CommandLine.Parameters(index = "1",paramLabel = "<paymentAmount>",
            description = "Payment amount")
    long amount;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;


    String BASE_URL = "http://localhost:8080/";

    @Override
    public void run() {
        LoginResponse loginResponse;
        try{
            loginResponse = readConfigFile();
        } catch (Exception e){
            throw new CommandLine.ExecutionException(spec.commandLine(), "Please login before you make payment");
        }
        try {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setSenderUserName(loginResponse.getUsername());
            paymentRequest.setRecipientUserName(recipientName);
            paymentRequest.setAmount(amount);
            PaymentResponse paymentResponse = TransactionApi.pay(BASE_URL, paymentRequest);
            System.out.println("Transferred " + amount + " to " + recipientName);
            CommandUtil.printDebt(paymentResponse.getDebt());
        } catch (ApiException e) {
            System.out.println(e.getErrorResponse().getMessage());
        }


    }

    private LoginResponse readConfigFile() throws Exception{
        String lines = Files.readAllLines(Paths.get("./fileName.txt")).get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        LoginResponse loginResponse = objectMapper.readValue(lines, LoginResponse.class);
        return loginResponse;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PaymentCommand()).execute(args);
        System.exit(exitCode);
    }
}
