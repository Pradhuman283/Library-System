package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.util.LinkedList;
import java.util.Queue;

@Entity
public class Book {

    @Id
    private String ISBN;

    private String title;
    private String author;
    private String genre;

    @Column(name = "is_available")
    private boolean isAvailable = true;
    private Queue<Integer> bookQ = new LinkedList<>();

    public Book() {}  // Required by JPA

    public Book(String ISBN, String title, String author, String genre) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }

    // Getters and Setters
    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public void addInQueue(int memberid){
        bookQ.add(memberid);
    }
    public boolean queueContains(int memberid){
        if(bookQ.contains(memberid)) return true;
        return false;
    }
    public boolean reserveQueueIsEmpty(){
        if(bookQ.isEmpty()) return true;
        return false;
    }

    public int popNextFromReservedQueue(){
        return bookQ.poll();
    }
}
