package com.example.ironlibrary;

import com.example.ironlibrary.model.Author;
import com.example.ironlibrary.model.Book;
import com.example.ironlibrary.model.Categories;
import com.example.ironlibrary.repository.AuthorRepository;
import com.example.ironlibrary.repository.BookRepository;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AuthorTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    Author author;
    Book book;

    @BeforeEach
    void setUp() {
        book = new Book("1234","my awesome book", Categories.ADVENTURE,3);
        bookRepository.save(book);
        author = new Author("Xavi","xavi@mail.com", book);
        authorRepository.save(author);
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }


    @Test
    public void createAuthor(){
        Optional<Author> authorFound = authorRepository.findById(author.getAuthorId());
        assertTrue(authorFound.isPresent());
        assertEquals(author.getAuthorId(),authorFound.get().getAuthorId());
    }


}