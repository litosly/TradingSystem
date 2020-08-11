package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class StartMenuSystem {
    private final Map<String, Response> commandMap;

    private interface Response {
        void response();
    }

    public StartMenuSystem() {
        commandMap = new HashMap<>();
        commandMap.put("1", () -> {
            try {
                LoginSystem loginSystem = new LoginSystem();
                loginSystem.run();
            } catch (Exception e) {
                System.out.println("Unexpected Error occurs while trying to enter Login system");
                System.exit(0);
            }
        });
        commandMap.put("2", () -> {
            try {
                SignUpSystem signUpSystem = new SignUpSystem();
                signUpSystem.run();
            } catch (Exception e) {
                System.out.println("Unexpected Error occurs while trying to enter Signup system");
                System.exit(0);
            }
        });
        commandMap.put("3", () -> { // Preserve for visitor system
            try {
                VisitorSystem visitorSystem = new VisitorSystem();
                visitorSystem.run();
            } catch (Exception e) {
                System.out.println("Unexpected Error occurs while trying to enter visitor system");
                System.exit(0);
            }
        });
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the start menu. At any time, type 'exit' to quit the system");
        String comm = "";
        while (true) {
            System.out.println("Type '1' to login, type '2' to sign up or type '3' to look around(phase 2)");
            try {
                comm = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (comm.equals("exit")) {
                System.exit(0);
            }
            if (!commandMap.containsKey(comm)) {
                System.out.println("Input not in range");
                continue;
            }
            commandMap.get(comm).response();
        }
    }
}
