package com.example.demo.command;

import com.example.demo.command.BankCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentCommandTest {

    @BeforeEach
    void setup() throws Exception {
        disableHttpLogging();
    }

    @Test
    @DisplayName("Ensure user able to make payment")
    void ensureUserAbleToMakePayment() throws Exception{
        String tester1 = "payment_tester1";
        String tester2 = "payment_tester2";
        BankCommand app = new BankCommand();
        CommandLine cmd = new CommandLine(app);
        cmd.execute("login", tester1);
        cmd.execute("login", tester2);
        cmd.execute("topup", "100");
        String outText = tapSystemOutNormalized(() -> {
            cmd.execute("pay", tester1, "50");
        });
        assertEquals("Transferred 50 to "+ tester1 +"\n" +
                "Your balance is 50\n", outText);
    }

    @Test
    @DisplayName("Ensure user not able to pay himself")
    void ensureUserNotAbleToMakePayment() throws Exception{
        String tester1 = "payment_tester3";
        String tester2 = "payment_tester4";
        BankCommand app = new BankCommand();
        CommandLine cmd = new CommandLine(app);
        cmd.execute("login", tester1);
        cmd.execute("login", tester2);
        cmd.execute("topup", "100");
        String outText = tapSystemOutNormalized(() -> {
            cmd.execute("pay", tester1, "50");
        });
        assertEquals("Transferred 50 to "+ tester1 +"\n" +
                "Your balance is 50\n", outText);
    }

    private static void disableHttpLogging(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
        root.setLevel(ch.qos.logback.classic.Level.INFO);
    }
}
