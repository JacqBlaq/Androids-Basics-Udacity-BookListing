package com.example.android.booklisting;

/**
 * Created by Jacquelyn Gboyor on 11/2/2017.
 */

public class BookDetails {

    private String bookImage;
    private String bookTitle;
    private String bookCategory;
    private String bookAuthor;
    private String bookPublisher;

    public BookDetails(String book, String title, String category, String author, String pub){
        bookImage = book;
        bookTitle = title;
        bookCategory = category;
        bookAuthor = author;
        bookPublisher = pub;
    }

    public String getBookImage() {
        return bookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

}//Class
