package com.example.demo.command;

import com.example.demo.command.BankCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopUpCommandTest {

    @BeforeEach
    void setup() throws Exception {
        disableHttpLogging();
    }

    @Test
    @DisplayName("Ensure user able to top up")
    void ensureUserAbleToTopUp() throws Exception{
        String tester1 = "topup_tester1";
        BankCommand app = new BankCommand();
        CommandLine cmd = new CommandLine(app);
        cmd.execute("login", tester1);
        String outText = tapSystemOutNormalized(() -> {
            cmd.execute("topup", "100");
        });
        assertEquals("Your balance is 100\n", outText);
    }

    private static void disableHttpLogging(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(ch.qos.logback.classic.Level.INFO);
    }
}
