package com.example.demo.controller;

import java.util.List;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;
import com.example.demo.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @PostMapping("/issue")
    public ResponseEntity<String> issueBook(@RequestParam String isbn,@RequestParam int member_id){
        String result = libraryService.issueBook(isbn,member_id);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam String isbn,@RequestParam int member_id){
        String result = libraryService.returnBook(isbn,member_id);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/book")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book savedBook = libraryService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @PostMapping("/member")
    public ResponseEntity<Member> addMember(@RequestBody Member member){
        Member savedBook = libraryService.addMember(member);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = libraryService.getAllabooks();
        return ResponseEntity.ok(books);

    }
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers(){
        List<Member> members = libraryService.getALlMembers();
        return ResponseEntity.ok(members);

    }

}
