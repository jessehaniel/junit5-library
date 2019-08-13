package dev.jessehaniel.phoebus.library.book;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Book Service - CRUD operations")
class BookServiceTest {
    
    @Mock
    private BookRepository repository;
    private BookService service;
    private List<BookDTO> bookListMock;
    
    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(repository);
        bookListMock = Arrays.asList(
                new BookDTO(1, "book1", "author1", "resume book1", "123", 2019),
                new BookDTO(2, "book2", "author1", "resume book2", "456", 2019),
                new BookDTO(3, "book3", "author2", "resume book3", "789", 2019)
        );
    }
    
    @Test
    @Tag("update")
    @DisplayName("Successful SAVE call")
    void save() {
        Mockito.when(repository.save(Mockito.any(Book.class))).thenAnswer(i -> {
            Book book = (Book) i.getArguments()[0];
            book.setId(bookListMock.size() + 1);
            return book;
        });
        
        BookDTO newBookDto = new BookDTO(null, "book4", "author3", "resume book4", "123456", 2018);
        BookDTO bookSaved = service.save(newBookDto);
        
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Book.class));
        MatcherAssert.assertThat(bookSaved, CoreMatchers.notNullValue());
        MatcherAssert.assertThat(bookSaved.getId(), CoreMatchers.notNullValue());
    }
    
    @Test
    @Tag("update")
    @DisplayName("Successful DELETE call")
    void delete() {
        Optional<Book> bookMock = bookListMock.stream().map(Book::of).findAny();
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(bookMock);
        
        int bookId = 0;
        service.delete(bookId);
        
        Mockito.verify(repository, Mockito.times(1)).findById(bookId);
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any(Book.class));
    }
    
    @Test
    @Tag("update")
    @DisplayName("Successful UPDATE call")
    void update() {
        Optional<Book> bookMock = bookListMock.stream().map(Book::of).findAny();
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(bookMock);
        Mockito.when(repository.save(Mockito.any(Book.class))).thenAnswer(i -> i.getArgument(0));
        
        Book book = bookMock.orElse(new Book());
        int bookId = book.getId();
        BookDTO bookToChange = Book.parseToDtoMono(book);
        bookToChange.setTitle(bookToChange.getTitle().toUpperCase());
        BookDTO bookDtoUpdated = service.update(bookId, bookToChange);
        
        Mockito.verify(repository, Mockito.times(1)).findById(bookId);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Book.class));
        MatcherAssert.assertThat(bookDtoUpdated.getId(), CoreMatchers.equalTo(bookId));
        MatcherAssert.assertThat(bookDtoUpdated.getTitle(), CoreMatchers.equalTo(book.getTitle().toUpperCase()));
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception throws assertion on DELETE when Book id not found")
    void deleteWithNotFoundException() {
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        int bookId = 4;
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> service.delete(bookId));
        Assertions.assertEquals("Book ID not found", exception.getMessage());
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception throws assertion on UPDATE when Book id not found")
    void updateWithNotFoundException() {
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        BookDTO bookDTO = new BookDTO(4, "book4", "author3", "resume book4", "123456", 2018);
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> service.update(bookDTO.getId(), bookDTO));
        Assertions.assertEquals("Book ID not found", exception.getMessage());
    }
    
    @Test
    @Tag("list")
    @DisplayName("List all call")
    void listAll() {
        List<Book> bookList = bookListMock.stream().map(Book::of).collect(Collectors.toList());
        Mockito.when(repository.findAll()).thenReturn(bookList);
        
        List<BookDTO> bookListActual = service.listAll();
        
        MatcherAssert.assertThat(bookListActual, CoreMatchers.is(bookListMock));
    }
}