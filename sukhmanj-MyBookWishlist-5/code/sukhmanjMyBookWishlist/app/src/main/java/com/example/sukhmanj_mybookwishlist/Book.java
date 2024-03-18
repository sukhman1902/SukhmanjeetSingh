/**
 * The `Book` class represents a book entity with various attributes such as
 * title, author, genre, publication year, and status (Read/Unread).
 */
package com.example.sukhmanj_mybookwishlist;

public class Book {
    private String title;// Title of the book
    private String author;// Author of the book
    private String genre;// genre of the book
    private int publicationYear;// year in which the book was published
    private String status;// status of the book either read or unread

    public Book(String title, String author, String genre, int publicationYear, String status) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.status = status;
    }
    // getters and setters

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public String getGenre() {

        return genre;
    }

    public void setGenre(String genre) {

        this.genre = genre;
    }

    public int getPublicationYear() {

        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {

        this.publicationYear = publicationYear;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }
}
