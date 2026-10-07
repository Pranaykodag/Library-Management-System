import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.loadFromFile(); // load any previously saved data

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Library Management System ===");

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addBook(scanner, library);
                        break;
                    case "2":
                        addMember(scanner, library);
                        break;
                    case "3":
                        issueBook(scanner, library);
                        break;
                    case "4":
                        returnBook(scanner, library);
                        break;
                    case "5":
                        listAllBooks(library);
                        break;
                    case "6":
                        listAvailableBooks(library);
                        break;
                    case "7":
                        searchBooks(scanner, library);
                        break;
                    case "8":
                        listAllMembers(library);
                        break;
                    case "9":
                        library.saveToFile();
                        System.out.println("Data saved. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (LibraryException e) {
                // Catching our custom exception here means the whole program
                // doesn't crash on bad input -- it just shows the error and
                // lets the user try again.
                System.out.println("Error: " + e.getMessage());
            }

            // Auto-save after every action so data is never lost, even if
            // the user closes the program without picking "Exit".
            library.saveToFile();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. List All Books");
        System.out.println("6. List Available Books");
        System.out.println("7. Search Books by Title");
        System.out.println("8. List All Members");
        System.out.println("9. Save and Exit");
        System.out.print("Choose an option: ");
    }

    private static void addBook(Scanner scanner, Library library) throws LibraryException {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();

        library.addBook(new Book(isbn, title, author));
        System.out.println("Book added successfully.");
    }

    private static void addMember(Scanner scanner, Library library) throws LibraryException {
        System.out.print("Enter Member ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        library.addMember(new Member(id, name));
        System.out.println("Member added successfully.");
    }

    private static void issueBook(Scanner scanner, Library library) throws LibraryException {
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();

        library.issueBook(isbn, memberId);
        System.out.println("Book issued successfully.");
    }

    private static void returnBook(Scanner scanner, Library library) throws LibraryException {
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.nextLine().trim();

        library.returnBook(isbn);
        System.out.println("Book returned successfully.");
    }

    private static void listAllBooks(Library library) {
        List<Book> books = library.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in the library yet.");
            return;
        }
        System.out.println("\nISBN         | Title                          | Author               | Status");
        System.out.println("-".repeat(90));
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void listAvailableBooks(Library library) {
        List<Book> books = library.getAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No books currently available.");
            return;
        }
        System.out.println("\nAvailable Books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void searchBooks(Scanner scanner, Library library) {
        System.out.print("Enter title keyword: ");
        String keyword = scanner.nextLine().trim();
        List<Book> results = library.searchByTitle(keyword);

        if (results.isEmpty()) {
            System.out.println("No books matched \"" + keyword + "\".");
            return;
        }
        System.out.println("\nSearch results:");
        for (Book book : results) {
            System.out.println(book);
        }
    }

    private static void listAllMembers(Library library) {
        List<Member> members = library.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members registered yet.");
            return;
        }
        System.out.println("\nMember ID  | Name");
        System.out.println("-".repeat(40));
        for (Member member : members) {
            System.out.println(member);
        }
    }
}
