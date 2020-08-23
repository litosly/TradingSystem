package presenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.LOGIN_PROMPT;

public class PromptPresenter {

    public static ArrayList<String> takeInput(String promptFile) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = new ArrayList<>();
        PropertiesIterator prompts = new PropertiesIterator(promptFile);

        try {
            String input = "";
            while (!input.equals("exit")) {
                if (!input.equals("exit")) { // != compares memory addresses.
                    if (prompts.hasNext()) {
                        System.out.println(prompts.next());
                    }
                    if (!prompts.hasNext()) {
                        break;
                    }
                }
            }
            input = br.readLine();
            inputArray.add(input);
            System.out.println(inputArray);
        } catch (IOException e) {
            System.out.println("Prompt Reading Error - Cannot open prompt text file.");
        }
        return inputArray;
    }

    public static ArrayList<String> takeInputLineByLine(String promptFile) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PropertiesIterator prompts = new PropertiesIterator(promptFile);

        ArrayList<String> inputArray = new ArrayList<>();
        try {
            String input = "";
            while (!input.equals("exit")) { // != compares memory addresses.
                if (prompts.hasNext()) {
                    System.out.println(prompts.next());
                }
                input = br.readLine();
                inputArray.add(input);
                if (!prompts.hasNext()) {
                    break;
                }

            }
            System.out.println(inputArray);
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
        return inputArray;
    }

}
