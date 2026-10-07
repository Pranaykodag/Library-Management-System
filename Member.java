/**
 * Represents a library member who can borrow books.
 */
public class Member {
    private String memberId;
    private String name;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String toFileFormat() {
        return memberId + "|" + name;
    }

    public static Member fromFileFormat(String line) {
        String[] parts = line.split("\\|", -1);
        return new Member(parts[0], parts[1]);
    }

    @Override
    public String toString() {
        return String.format("%-10s | %s", memberId, name);
    }
}
