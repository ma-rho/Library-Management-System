package library.management.model;

public class Book {
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private boolean isAvailable;

    // Constructor, Getters, and Setters
    public Book(int bookId, String isbn, String title, String author, int publicationYear, boolean isAvailable) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isAvailable = isAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
