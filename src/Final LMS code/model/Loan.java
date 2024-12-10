package library.management.model;

import java.util.Date;

public class Loan {
    private int loanId;
    private int bookId;
    private int userId;
    private Date loanDate;
    private Date dueDate;
    private Date returnDate;
    private double fine;

    // Constructor, Getters, and Setters
    public Loan(int loanId, int bookId, int userId, Date loanDate, Date dueDate, Date returnDate, double fine) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
    }

    public int getLoanId() {
        return loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getFine() {
        return fine;
    }
}
