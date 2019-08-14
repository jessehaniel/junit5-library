package dev.jessehaniel.phoebus.library.book;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for BOOK Service - CRUD operations")
@Tag("fast")
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
        when(repository.save(Mockito.any(Book.class))).thenAnswer(i -> {
            Book book = (Book) i.getArguments()[0];
            book.setId(bookListMock.size() + 1);
            return book;
        });
        
        BookDTO newBookDto = new BookDTO(null, "book4", "author3", "resume book4", "123456", 2018);
        BookDTO bookSaved = service.save(newBookDto);
        
        verify(repository, Mockito.times(1)).save(Mockito.any(Book.class));
        assertThat(bookSaved, CoreMatchers.notNullValue());
        assertThat(bookSaved.getId(), CoreMatchers.notNullValue());
    }
    
    @Test
    @Tag("update")
    @DisplayName("Successful DELETE call")
    void delete() {
        Optional<Book> bookMock = bookListMock.stream().map(Book::of).findAny();
        when(repository.findById(anyInt())).thenReturn(bookMock);
        
        int bookId = 0;
        service.delete(bookId);
        
        verify(repository, Mockito.times(1)).findById(bookId);
        verify(repository, Mockito.times(1)).delete(Mockito.any(Book.class));
    }
    
    @Test
    @Tag("update")
    @DisplayName("Successful UPDATE call")
    void update() {
        Optional<Book> bookMock = bookListMock.stream().map(Book::of).findAny();
        when(repository.findById(anyInt())).thenReturn(bookMock);
        when(repository.save(Mockito.any(Book.class))).thenAnswer(i -> i.getArgument(0));
        
        Book book = bookMock.orElse(new Book());
        int bookId = book.getId();
        BookDTO bookToChange = Book.parseToDtoMono(book);
        bookToChange.setTitle(bookToChange.getTitle().toUpperCase());
        BookDTO bookDtoUpdated = service.update(bookId, bookToChange);
        
        verify(repository, Mockito.times(1)).findById(bookId);
        verify(repository, Mockito.times(1)).save(Mockito.any(Book.class));
        assertThat(bookDtoUpdated.getId(), equalTo(bookId));
        assertThat(bookDtoUpdated.getTitle(), equalTo(book.getTitle().toUpperCase()));
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception throws assertion on DELETE when Book id not found")
    void deleteWithNotFoundException() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        int bookId = 4;
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.delete(bookId));
        assertEquals("Book ID not found", exception.getMessage());
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception throws assertion on UPDATE when Book id not found")
    void updateWithNotFoundException() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        BookDTO bookDTO = new BookDTO(4, "book4", "author3", "resume book4", "123456", 2018);
        
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.update(bookDTO.getId(), bookDTO));
        assertEquals("Book ID not found", exception.getMessage());
    }
    
    @Test
    @Tag("list")
    @DisplayName("List all call")
    void listAll() {
        when(repository.findAll()).thenReturn(Book.ofList(bookListMock));
        List<BookDTO> bookListActual = service.listAll();
        assertThat(bookListActual, CoreMatchers.is(bookListMock));
    }
}