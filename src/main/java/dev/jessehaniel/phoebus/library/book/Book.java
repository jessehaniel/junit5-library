package dev.jessehaniel.phoebus.library.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String authorName;
    private String resume;
    private String isbn;
    private int yearRelease;
    
    static Book of(@NotNull BookDTO bookDTO) {
        return new Book(bookDTO.getId(), bookDTO.getTitle(), bookDTO.getAuthorName(), bookDTO.getResume(), bookDTO.getIsbn(),
                bookDTO.getYearRelease());
    }
    
    static List<BookDTO> parseToDtoList(@NotEmpty List<Book> bookList) {
        return bookList.stream()
                .map(Book::parseToDtoMono)
                .collect(Collectors.toList());
    }
    
    static BookDTO parseToDtoMono(@NotNull Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthorName(), book.getResume(), book.getIsbn(), book.getYearRelease());
    }
}
