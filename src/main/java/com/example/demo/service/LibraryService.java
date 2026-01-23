package com.example.demo.service;
import java.util.List;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;
import com.example.demo.exception.AlreadyRegistredForBookException;
import com.example.demo.exception.BookAlreadyIssuedException;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.MemberNotFoundException;
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

    public void issueBook(String isbn, int memberId) {
        /*
        Optional<Book> bookOpt = bookRepository.findById(isbn);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        Book book ;
        Member member;



        if (bookOpt.isPresent()) {
              book = bookOpt.get();
        }
        else{
            throw new BookNotFoundException("Book not found");
        }

       if (memberOpt.isPresent()){
          member = memberOpt.get();
       }
       else{
           throw new MemberNotFoundException("Member not found");
       }
       */
       Book book = bookRepository.findById(isbn)
               .orElseThrow( () ->
                       new BookNotFoundException("Book not found with "+isbn)
               );

       Member member = memberRepository.findById(memberId)
               .orElseThrow(() ->
                       new MemberNotFoundException("Member not found with "+ memberId)

               );

       if(!book.isAvailable()){
           if(book.queueContains(member.getMemberId())){
               throw new AlreadyRegistredForBookException("Member already Registred");
           }
           book.addInQueue(member.getMemberId());
           bookRepository.save(book);

           return;
       }
        member.getBorrowedBooks().add(book);
        book.setAvailable(false);

        memberRepository.save(member);
        bookRepository.save(book);
    }

    public void returnBook(String isbn, int memberId) {
        /*
        Optional<Book> bookOpt = bookRepository.findById(isbn);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (bookOpt.isEmpty()) throw new BookNotFoundException("Book is not found");
        if (memberOpt.isEmpty()) throw new MemberNotFoundException("Member not found");

        Book book = bookOpt.get();
        Member member = memberOpt.get();
        */
        Book book = bookRepository.findById(isbn)
                .orElseThrow( () ->
                        new BookNotFoundException("Book not found with "+isbn)
                );

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found with "+ memberId)

                );

        member.getBorrowedBooks().remove(book);
        // auto issuing of the book
        if(!book.reserveQueueIsEmpty()){
            int nextMemberId = book.popNextFromReservedQueue();
                    Member nextMember = memberRepository.findById(nextMemberId)
                    .orElseThrow(() ->
                            new MemberNotFoundException("Queued member not found")
                    );

            nextMember.getBorrowedBooks().add(book);
            memberRepository.save(nextMember);
        }
        else {

            book.setAvailable(true);
        }
        memberRepository.save(member);
        bookRepository.save(book);
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

    public List<Member> getALlMembers(){
        return memberRepository.findAll();
    }


}