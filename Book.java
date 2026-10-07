/**
 * Represents a single book in the library.
 * Keeping this as its own class (instead of just using arrays of strings)
 * is a basic OOP principle: each real-world "thing" gets its own class.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    private boolean isIssued;
    private String issuedTo; // member ID, null if not issued

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedTo = null;
    }

    // Getters and setters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void issueTo(String memberId) {
        this.isIssued = true;
        this.issuedTo = memberId;
    }

    public void returnBook() {
        this.isIssued = false;
        this.issuedTo = null;
    }

    /**
     * Converts this book into a single line of text so it can be saved to a file.
     * Format: isbn|title|author|isIssued|issuedTo
     */
    public String toFileFormat() {
        return isbn + "|" + title + "|" + author + "|" + isIssued + "|" + (issuedTo == null ? "" : issuedTo);
    }

    /**
     * Rebuilds a Book object from a saved line of text (the reverse of toFileFormat).
     */
    public static Book fromFileFormat(String line) {
        String[] parts = line.split("\\|", -1);
        Book book = new Book(parts[0], parts[1], parts[2]);
        boolean issued = Boolean.parseBoolean(parts[3]);
        if (issued && !parts[4].isEmpty()) {
            book.issueTo(parts[4]);
        }
        return book;
    }

    @Override
    public String toString() {
        String status = isIssued ? ("Issued to " + issuedTo) : "Available";
        return String.format("%-12s | %-30s | %-20s | %s", isbn, title, author, status);
    }
}
