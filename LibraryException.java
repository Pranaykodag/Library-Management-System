/**
 * A custom checked exception for library-specific errors.
 * Using a custom exception (instead of just printing an error) is a common
 * Java interview topic -- it lets calling code decide how to handle the
 * problem instead of the method deciding for them.
 */
public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}
