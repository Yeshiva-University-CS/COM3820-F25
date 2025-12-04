import java.util.*;
import java.util.stream.*;


public class Book {
    String title;
    String author;
    int year;
    double price;

    public Book(String title, String author, int year, double price) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (" + year + ") - $" + price;
    }

    public static void main(String[] args) {
        List<Book> library = Arrays.asList(
                new Book("1984", "George Orwell", 1949, 15.99),
                new Book("To Kill a Mockingbird", "Harper Lee", 1960, 12.99),
                new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, 10.99),
                new Book("Animal Farm", "George Orwell", 1945, 9.99),
                new Book("Brave New World", "Aldous Huxley", 1932, 14.99),
                new Book("Lord of the Flies", "William Golding", 1954, 11.99));

        // Exercise 5: Find all books by George Orwell
        List<Book> orwellBooks = new ArrayList<>();

        System.out.println("Books by George Orwell: " + orwellBooks);

        // Exercise 6: Find all books published after 1950
        List<Book> booksAfter1950 = new ArrayList<>();

        System.out.println("Books published after 1950: " + booksAfter1950);

        // Exercise 7: Get a list of all book titles (just the strings)
        List<String> bookTitles = new ArrayList<>();

        System.out.println("Book Titles: " + bookTitles);

        // Exercise 8: Calculate the total value of all books in the library
        double totalValue = 0.0;

        System.out.println("Total Value of Books: $" + totalValue);

        // Exercise 9: Find the most expensive book
        Book mostExpensiveBook = null;

        System.out.println("Most Expensive Book: " + mostExpensiveBook);
    }
}
