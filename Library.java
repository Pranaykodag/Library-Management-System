import java.io.*;
import java.util.*;

/**
 * The Library class holds all the business logic: adding books, issuing,
 * returning, searching, and saving/loading data to a file so records
 * persist between runs of the program.
 *
 * Using a HashMap keyed by ISBN/memberId gives us fast O(1) average-time
 * lookups instead of looping through a list every time (an ArrayList would
 * need O(n) search) -- a good, genuine example of choosing the right data
 * structure for the job.
 */
public class Library {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();

    private static final String BOOKS_FILE = "books.txt";
    private static final String MEMBERS_FILE = "members.txt";

    // ---------- Book operations ----------

    public void addBook(Book book) throws LibraryException {
        if (books.containsKey(book.getIsbn())) {
            throw new LibraryException("A book with ISBN " + book.getIsbn() + " already exists.");
        }
        books.put(book.getIsbn(), book);
    }

    public void issueBook(String isbn, String memberId) throws LibraryException {
        Book book = books.get(isbn);
        if (book == null) {
            throw new LibraryException("No book found with ISBN " + isbn);
        }
        if (!members.containsKey(memberId)) {
            throw new LibraryException("No member found with ID " + memberId);
        }
        if (book.isIssued()) {
            throw new LibraryException("Book \"" + book.getTitle() + "\" is already issued to " + book.getIssuedTo());
        }
        book.issueTo(memberId);
    }

    public void returnBook(String isbn) throws LibraryException {
        Book book = books.get(isbn);
        if (book == null) {
            throw new LibraryException("No book found with ISBN " + isbn);
        }
        if (!book.isIssued()) {
            throw new LibraryException("Book \"" + book.getTitle() + "\" was not issued to anyone.");
        }
        book.returnBook();
    }

    public List<Book> searchByTitle(String keyword) {
        List<Book> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(lowerKeyword)) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isIssued()) {
                available.add(book);
            }
        }
        return available;
    }

    // ---------- Member operations ----------

    public void addMember(Member member) throws LibraryException {
        if (members.containsKey(member.getMemberId())) {
            throw new LibraryException("A member with ID " + member.getMemberId() + " already exists.");
        }
        members.put(member.getMemberId(), member);
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // ---------- File persistence ----------

    /**
     * Saves all books and members to text files so data survives between runs.
     * Uses try-with-resources so the file writer is automatically closed even
     * if an error happens partway through writing.
     */
    public void saveToFile() {
        try (PrintWriter bookWriter = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books.values()) {
                bookWriter.println(book.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }

        try (PrintWriter memberWriter = new PrintWriter(new FileWriter(MEMBERS_FILE))) {
            for (Member member : members.values()) {
                memberWriter.println(member.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    /**
     * Loads books and members from text files when the program starts.
     * If the files don't exist yet (first run), it just starts empty.
     */
    public void loadFromFile() {
        File bookFile = new File(BOOKS_FILE);
        if (bookFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        Book book = Book.fromFileFormat(line);
                        books.put(book.getIsbn(), book);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading books: " + e.getMessage());
            }
        }

        File memberFile = new File(MEMBERS_FILE);
        if (memberFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(memberFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        Member member = Member.fromFileFormat(line);
                        members.put(member.getMemberId(), member);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading members: " + e.getMessage());
            }
        }
    }
}
