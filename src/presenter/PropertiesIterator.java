package presenter;

import gateway.FileReadAndWrite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


//reference: ReadWritEx project
public class PropertiesIterator implements Iterator<String> {
    private List<String> properties = new ArrayList<>();
    private int current = 0;


    public PropertiesIterator(String fileName) {
        String promptInfo = FileReadAndWrite.readFromFile(fileName);
        String[] promptInfoArray = promptInfo.split(System.lineSeparator());
        for(String text: promptInfoArray){
            properties.add(text);
        }
    }

    /**
     * Checks for subsequent prompts.
     * @return true if there is prompt that has not yet been returned.
     */
    @Override
    public boolean hasNext() {
        return current < properties.size();
    }

    /**
     * Returns the next prompt to be printed.
     * @return the next prompt.
     */
    @Override
    public String next() {
        String res;

        try {
            res = properties.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }

    /**
     * Removes the prompt just returned. Unsupported.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

}
