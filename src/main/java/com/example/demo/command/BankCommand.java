package com.example.demo.command;

import com.example.demo.command.subcommand.LoginCommand;
import com.example.demo.command.subcommand.PaymentCommand;
import com.example.demo.command.subcommand.TopUpCommand;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static picocli.CommandLine.Command;
@Command(name = "bank",
        subcommands = { LoginCommand.class, PaymentCommand.class, TopUpCommand.class})
public class BankCommand implements Runnable{
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Specify a subcommand");
    }

    public static void main(String[] args) {
        disableHttpLogging();
        int exitCode = new CommandLine(new BankCommand()).execute(args);
        System.exit(exitCode);
    }

    private static void disableHttpLogging(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(ch.qos.logback.classic.Level.INFO);
    }

}
