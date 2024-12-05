package frontend.Error;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class ErrorLog {
    private static final ErrorLog instance = new ErrorLog();

    private final ArrayList<Error> errors = new ArrayList<>();

    public static ErrorLog getInstance() {
        return instance;
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public void printErrors() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Collections.sort(this.errors);
        for (Error error : errors) {
            stringBuilder.append(error.toString()).append("\n");
        }
        Path path = Paths.get("error.txt");
        Files.writeString(path, stringBuilder.toString());
    }
}
