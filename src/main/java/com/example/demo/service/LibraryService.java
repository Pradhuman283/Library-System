package com.example.demo.service;
import java.util.List;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    public String issueBook(String isbn, int memberId) {
        Optional<Book> bookOpt = bookRepository.findById(isbn);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (bookOpt.isEmpty()) return "Book not found";
        if (memberOpt.isEmpty()) return "Member not found";

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        if (!book.isAvailable()) return "Book is already issued";

        member.getBorrowedBooks().add(book);
        book.setAvailable(false);

        memberRepository.save(member);
        bookRepository.save(book);

        return "Book \"" + book.getTitle() + "\" issued to " + member.getName();
    }

    public String returnBook(String isbn, int memberId) {
        Optional<Book> bookOpt = bookRepository.findById(isbn);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (bookOpt.isEmpty()) return "Book not found";
        if (memberOpt.isEmpty()) return "Member not found";

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        member.getBorrowedBooks().remove(book);
        book.setAvailable(true);

        memberRepository.save(member);
        bookRepository.save(book);

        return "Book \"" + book.getTitle() + "\" returned by " + member.getName();
    }
    public Book addBook(Book book){
        return bookRepository.save(book);
    }
    public Member addMember(Member member){
        return memberRepository.save(member);
    }
    public List<Book> getAllabooks(){
        return bookRepository.findAll();
    }

}