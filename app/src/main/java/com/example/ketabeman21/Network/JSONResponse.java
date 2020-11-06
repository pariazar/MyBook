package com.example.ketabeman21.Network;


import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.Model.Category;

public class JSONResponse {
    private Book[] book;
    private Category[] category;

    public Book[] getBook() {
        return book;
    }

    public Category[] getCategory() {
        return category;
    }
}
