package com.ironhack.viewer;

import com.ironhack.model.Book;
import com.ironhack.model.Categories;
import com.ironhack.model.Issue;
import com.ironhack.model.Student;
import com.ironhack.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class LibraryMenu {
    @Autowired
    private LibraryService libraryService;

    private Optional<Book> one_book=null;
    private List<Book> books;

    public LibraryMenu(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book By Title");
            System.out.println("3. Search Book By Category");
            System.out.println("4. Search Book By Author");
            System.out.println("5. Search All Books");
            System.out.println("6. Issue Book");
            System.out.println("7. Return Book");
            System.out.println("8. Search Books By Student");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            try{
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            }catch (InputMismatchException ime){
                choice = 9;
                scanner.nextLine();
            }
            switch (choice) {
                case 1:
                    // Add Book
                    break;
                case 2:
                    System.out.print("Enter title to search: ");
                    String title = scanner.nextLine();
                    one_book=libraryService.searchBookByTitle(title);
                    if(one_book.isPresent()) {
                        System.out.println(one_book.get().getIsbn());
                        System.out.println(one_book.get().getTitle());
                    }
                    break;
                case 3:
                    System.out.print("Enter a Category to search: ");
                    String category = scanner.nextLine();
                    try{
                        books = libraryService.searchBookByCategory(Categories.valueOf(category));
                        for(Book book: books){
                            System.out.println(book.getIsbn());
                            System.out.println(book.getTitle());
                        }
                    }
//                    catch (InputMismatchException imm){
//                        System.out.print("Wrong Category ");
//                    }
                    catch (IllegalArgumentException iae){
                        System.out.println("Wrong Category");
                    }

                    break;
                case 4:
                    System.out.print("Enter an Author ID:");
                    String author_id = scanner.nextLine();
                    try{
                        //TODO change to isbn validation
                        int authorid = Integer.parseInt(author_id);
                        books = libraryService.searchBookByAuthor(authorid);
                        for(Book book: books){
                            System.out.println(book.getIsbn());
                            System.out.println(book.getTitle());
                        }
                    }catch (IllegalArgumentException iae){
                        System.out.println("Author ID should be numeric");
                    }

                    break;
                case 5:
                    // Search All Books
                    books = libraryService.searchAllBooks();
                    for(Book book: books){
                        System.out.println(book.getIsbn());
                        System.out.println(book.getTitle());
                    }
                    break;
                case 6:
                    // Issue Book
                    System.out.print("Enter usn:");
                    String usn = scanner.nextLine();
                    System.out.print("Enter name:");
                    String name = scanner.nextLine();
                    System.out.print("Enter book ISBN:");
                    String isbn = scanner.nextLine();
                    try{
                        //check if student and book exist. Comprobar nombre?????
                        Optional<Student> studentOptional = libraryService.findStudentByUsn(usn);
                        Optional<Book> bookOptional = libraryService.findBookByIsbn(isbn);
                        if (studentOptional.isPresent() && bookOptional.isPresent()) {
                            libraryService.issueBook(usn, name, isbn);
                            Optional<Issue> issue = libraryService.findIssueByIsbn(isbn);
                            System.out.println("");
                            System.out.println("Book issued. Return date : " + issue.get().getReturnDate());
                        } else {
                            System.out.println("Student or book does not exist.");
                        }
                    } catch (IllegalArgumentException iae){
                        System.out.println("An exception occurred: " + iae.getMessage());
                }
                    break;
                case 7:
                    // Return Book
                    break;
                case 8:
                    // Search Books By Student
                    System.out.println("Enter usn:");
                    String usnSearch = scanner.nextLine();
                    try{
                        Optional<Student> studentOptional = libraryService.findStudentByUsn(usnSearch);
                        if (studentOptional.isPresent()){
                            List<Issue> issueList = libraryService.searchBooksByStudentString(usnSearch);
                            if (issueList.isEmpty()) {
                                System.out.println("No books found for the specified student.");
                                break;
                            }
                            System.out.println("Book Title          Student Name    Return date");
                            for (Issue issue : issueList) {
                                String bookTitle = issue.getIssueBook().getTitle();
                                String studentName = issue.getIssueStudent().getName();
                                String returnDate = issue.getReturnDate();
                                System.out.printf("%-20s %-15s %s%n", bookTitle, studentName, returnDate);
                            }

                        } else{
                            System.out.println("Student does not exist");
                        }

                    } catch(IllegalArgumentException iae) {
                        System.out.println("An exception occurred: " + iae.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }

    public static void main(String[] args) {
        LibraryService libraryService = new LibraryService();
        LibraryMenu libraryMenu = new LibraryMenu(libraryService);
        libraryMenu.displayMenu();
    }
}