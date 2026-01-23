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
    public ResponseEntity<Void> issueBook(@RequestParam String isbn,@RequestParam int member_id){
        libraryService.issueBook(isbn,member_id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/return")
    public ResponseEntity<Void> returnBook(@RequestParam String isbn,@RequestParam int member_id){
        libraryService.returnBook(isbn,member_id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/book")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book savedBook = libraryService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @PostMapping("/member")
    public ResponseEntity<Member> addMember(@RequestBody Member member){
        Member savedMember = libraryService.addMember(member);
        return ResponseEntity.ok(savedMember);
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
