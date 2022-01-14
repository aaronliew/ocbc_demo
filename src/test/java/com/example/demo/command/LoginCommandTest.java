package com.example.demo.command;

import com.example.demo.command.BankCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class LoginCommandTest {

    @BeforeEach
    void setup() throws Exception {
        disableHttpLogging();
    }

    @Test
    @DisplayName("Ensure user able to login through command line")
    void ensureUserAbleToLogin() throws Exception{
        String tester1 = "login_tester1";
        BankCommand app = new BankCommand();
        CommandLine cmd = new CommandLine(app);
        String outText = tapSystemOutNormalized(() -> {
            cmd.execute("login", tester1);

        });
        assertEquals("Hello, login_tester1\n" +
                "Your balance is 0\n", outText);
    }

    private static void disableHttpLogging(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(ch.qos.logback.classic.Level.INFO);
    }
}
