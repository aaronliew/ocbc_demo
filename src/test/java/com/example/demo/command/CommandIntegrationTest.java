package com.example.demo.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandIntegrationTest {

    @BeforeEach
    void setup() throws Exception {
        disableHttpLogging();
    }

    @Test
    @DisplayName("Ensure user able to login through command line")
    void ensureUserAbleToLogin() throws Exception{
        BankCommand app = new BankCommand();
        CommandLine cmd = new CommandLine(app);
        cmd.execute("login", "alice");
        cmd.execute("topup", "100");
        cmd.execute("login", "bob");
        cmd.execute("topup", "80");
        String transfer50ToAlice = tapSystemOutNormalized(() -> {
            cmd.execute("pay", "alice", "50");
        });
        assertEquals("Transferred 50 to alice\n" +
                "Your balance is 30\n", transfer50ToAlice);

        String transfer100ToAlice = tapSystemOutNormalized(() -> {
            cmd.execute("pay", "alice", "100");
        });

        assertEquals("Transferred 30 to alice\n" +
                "Your balance is 0\n" +
                "Owing 70 to alice\n", transfer100ToAlice);

        String bobTopUp30 = tapSystemOutNormalized(() -> {
            cmd.execute("topup", "30");
        });

        assertEquals("Transferred 30 to alice\n" +
                "Your balance is 0\n" +
                "Owing 40 to alice\n", bobTopUp30);


        String loginAsAlice = tapSystemOutNormalized(() -> {
            cmd.execute("login", "alice");
        });

        assertEquals("Hello, alice\n" +
                        "Your balance is 210\n" +
                        "Owing 40 from bob\n", loginAsAlice);

        String transfer30ToBob = tapSystemOutNormalized(() -> {
            cmd.execute("pay", "bob", "30");
        });
        assertEquals("Transferred 30 to bob\n" +
                        "Your balance is 210\n" +
                        "Owing 10 from bob\n"
                        , transfer30ToBob);

        cmd.execute("login", "bob");

        String bobTopUp100 = tapSystemOutNormalized(() -> {
            cmd.execute("topup", "100");
        });

        assertEquals("Transferred 10 to alice\n" +
                "Your balance is 90\n", bobTopUp100);

    }

    private static void disableHttpLogging(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(ch.qos.logback.classic.Level.INFO);
    }
}
