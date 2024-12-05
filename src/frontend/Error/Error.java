package frontend.Error;

public class Error implements Comparable<Error> {
    private final Integer row;
    private final String type;

    public Error(Integer row, String type) {
        this.row = row;
        this.type = type;
    }

    public Integer getRow() {
        return this.row;
    }

    public int compareTo(Error other) {
        return this.row.compareTo(other.getRow());
    }

    public String toString() {
        return row + " " + type;
    }
}
