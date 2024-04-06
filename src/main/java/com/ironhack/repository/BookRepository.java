package com.ironhack.repository;


import com.ironhack.model.Book;
import com.ironhack.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findBookByTitle(String title);

    List<Book> findBookByCategory(Categories category);

    @Query("Select b FROM Book b JOIN b.authorBook a WHERE a.authorId = :authorId")
    List<Book> findBookByAuthorId(int authorId);

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN FETCH b.authorBook")
    List<Book> findAllBooksWithAuthor();

}
