package controller;

import java.io.IOException;
import java.util.ArrayList;

public interface InputProcessable {
    void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException;
}
