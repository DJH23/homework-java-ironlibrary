package com.ironhack.service;

import com.ironhack.model.*;
import com.ironhack.repository.AuthorRepository;
import com.ironhack.repository.BookRepository;
import com.ironhack.repository.IssueRepository;
import com.ironhack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ironhack.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    StudentRepository studentRepository;

    public void addNewBook(Book book) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        if (optionalBook.isPresent()) {
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);
        } else {
            bookRepository.save(book);
        }
    }

    public Optional<Book> searchBookByTitle(String title) {
        return bookRepository.findBookByTitle(title);
    }

    public List<Book> searchBookByCategory(Categories category) {
        return bookRepository.findBookByCategory(category);
    }

    public List<Book> searchBookByAuthor(int author_id) {
        return bookRepository.findBookByAuthorId(author_id);
    }

    public List<Book> searchAllBooks() {
        return bookRepository.findAll();
    }

    public String issueBook(String usn, String name, String isbn) {
        LocalDateTime issueDate = LocalDateTime.now();
        LocalDateTime returnDate = issueDate.plusDays(7);
        String issueDateString = Utils.formatter.format(issueDate);
        String returnDateString = Utils.formatter.format(returnDate);
        Issue issue = new Issue(issueDateString, returnDateString);
        Optional<Student> student = studentRepository.findByUsn(usn);
        //check if book is already issued
        if (!isBookIssued(isbn)) {
            issue.setIssueStudent(student.get());
            Optional<Book> book = bookRepository.findByIsbn(isbn);
            issue.setIssueBook(book.get());
            issueRepository.save(issue);
            //restar un ejemplar a libro
            book.get().updateQuantity(-1);
            bookRepository.save(book.get());
            return returnDateString;
        } else {
            return null;
        }
    }

    public boolean isBookIssued(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        return book.get().getQuantity() == 0;
    }

    public List<Issue> searchBooksByStudentString(String usn) {
        return studentRepository.searchBooksByStudent(usn);
    }

    public List<Issue> findIssueByIsbn(String isbn) {
        return issueRepository.findByIsbn(isbn);
    }

    public List<Issue> findIssueByUsn(String usn) {
        return issueRepository.findByUsn(usn);
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public Optional<Student> findStudentByUsn(String usn) {
        return studentRepository.findByUsn(usn);
    }

    public List<Book> findAllBooksWithAuthors() {
        return bookRepository.findAllBooksWithAuthor();
    }

    //añadido hoy
    public Optional<Author> findAuthorByName(String name) {
        return authorRepository.findByName(name);
    }
}
