package library.management.gui;

import library.management.db.DatabaseConnection;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.Mockito.*;

public class ManageBooksPageTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws Exception {
        // Mocking the database connection and its related components
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Set up the mock behavior for DatabaseConnection
        DatabaseConnection.setTestConnection(mockConnection);
    }

    @Test
    public void testLoadBooksData() throws Exception {
        // Mocking ResultSet to return sample book data
        when(mockConnection.prepareStatement("SELECT * FROM books")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false); // Two rows
        when(mockResultSet.getInt("book_id")).thenReturn(1, 2);
        when(mockResultSet.getString("isbn")).thenReturn("12345", "67890");
        when(mockResultSet.getString("title")).thenReturn("Book A", "Book B");
        when(mockResultSet.getString("author")).thenReturn("Author A", "Author B");
        when(mockResultSet.getInt("publication_year")).thenReturn(2000, 2001);
        when(mockResultSet.getBoolean("is_available")).thenReturn(true, false);

        ManageBooksPage manageBooksPage = new ManageBooksPage(null);
        manageBooksPage.loadBooksData();

        // Verify the interactions
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next(); // Two rows and one end of data
    }

    @Test
    public void testAddBook() throws Exception {
        String isbn = "12345";
        String title = "New Book";
        String author = "New Author";
        int year = 2023;
        boolean available = true;

        when(mockConnection.prepareStatement("INSERT INTO books (isbn, title, author, publication_year, is_available) VALUES (?, ?, ?, ?, ?)"))
                .thenReturn(mockPreparedStatement);

        ManageBooksPage manageBooksPage = new ManageBooksPage(null);
        manageBooksPage.isbnField.setText(isbn);
        manageBooksPage.titleField.setText(title);
        manageBooksPage.authorField.setText(author);
        manageBooksPage.yearField.setText(String.valueOf(year));
        manageBooksPage.availableField.setText(String.valueOf(available));
        manageBooksPage.addBook();

        // Verify the PreparedStatement parameters and execution
        verify(mockPreparedStatement).setString(1, isbn);
        verify(mockPreparedStatement).setString(2, title);
        verify(mockPreparedStatement).setString(3, author);
        verify(mockPreparedStatement).setInt(4, year);
        verify(mockPreparedStatement).setBoolean(5, available);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateBook() throws Exception {
        int bookId = 1;
        String isbn = "54321";
        String title = "Updated Book";
        String author = "Updated Author";
        int year = 2022;
        boolean available = false;

        when(mockConnection.prepareStatement("UPDATE books SET isbn = ?, title = ?, author = ?, publication_year = ?, is_available = ? WHERE book_id = ?"))
                .thenReturn(mockPreparedStatement);

        ManageBooksPage manageBooksPage = new ManageBooksPage(null);
        manageBooksPage.isbnField.setText(isbn);
        manageBooksPage.titleField.setText(title);
        manageBooksPage.authorField.setText(author);
        manageBooksPage.yearField.setText(String.valueOf(year));
        manageBooksPage.availableField.setText(String.valueOf(available));
        manageBooksPage.booksTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{{bookId, "12345", "Old Title", "Old Author", 2021, true}},
                new String[]{"Book ID", "ISBN", "Title", "Author", "Year", "Available"}
        ));
        manageBooksPage.booksTable.setRowSelectionInterval(0, 0); // Select the first row
        manageBooksPage.updateBook();

        // Verify the PreparedStatement parameters and execution
        verify(mockPreparedStatement).setString(1, isbn);
        verify(mockPreparedStatement).setString(2, title);
        verify(mockPreparedStatement).setString(3, author);
        verify(mockPreparedStatement).setInt(4, year);
        verify(mockPreparedStatement).setBoolean(5, available);
        verify(mockPreparedStatement).setInt(6, bookId);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteBook() throws Exception {
        int bookId = 1;

        when(mockConnection.prepareStatement("DELETE FROM books WHERE book_id = ?")).thenReturn(mockPreparedStatement);

        ManageBooksPage manageBooksPage = new ManageBooksPage(null);
        manageBooksPage.booksTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{{bookId, "12345", "Title", "Author", 2021, true}},
                new String[]{"Book ID", "ISBN", "Title", "Author", "Year", "Available"}
        ));
        manageBooksPage.booksTable.setRowSelectionInterval(0, 0); // Select the first row
        manageBooksPage.deleteBook();

        // Verify the PreparedStatement parameters and execution
        verify(mockPreparedStatement).setInt(1, bookId);
        verify(mockPreparedStatement).executeUpdate();
    }

    @AfterEach
    public void tearDown() {
        DatabaseConnection.setTestConnection(null); // Reset the connection
    }
}
