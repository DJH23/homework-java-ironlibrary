package com.ironhack.service;

import com.ironhack.model.Book;
import com.ironhack.model.Categories;
import com.ironhack.model.Issue;
import com.ironhack.model.Student;
import com.ironhack.repository.AuthorRepository;
import com.ironhack.repository.BookRepository;
import com.ironhack.repository.IssueRepository;
import com.ironhack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void issueBook(String isbn, String usn) {
    }

    private void issueBook(String usn, String name, String isbn) {
        LocalDateTime issueDate = LocalDateTime.now();
        LocalDateTime returnDate = issueDate.plusDays(7);
        //mover a Utils:
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String todayString = FORMATTER.format(issueDate);
        String returnDateString = FORMATTER.format(returnDate);

        Issue issue = new Issue(todayString, returnDateString);
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
        } else {
            System.out.println("Book is already issued");
        }
    }

    public boolean isBookIssued(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        return book.get().getQuantity() == 0;
    }

    private List<Issue> searchBooksByStudentString(String usn) {
        return studentRepository.searchBooksByStudent(usn);
    }
}
