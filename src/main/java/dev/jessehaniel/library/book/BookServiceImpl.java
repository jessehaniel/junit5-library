package dev.jessehaniel.library.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    
    private final BookRepository repository;
    
    @Override
    public BookDTO save(BookDTO bookDTO) {
        Book book = Book.of(bookDTO);
        book.setId(null);
        return Book.parseToDtoMono(repository.save(book));
    }
    
    @Override
    public void delete(int bookId) {
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book ID not found"));
        repository.delete(book);
    }
    
    @Override
    public BookDTO update(int bookId, BookDTO bookDTO) {
        if (repository.findById(bookId).isPresent()) {
            Book book = Book.of(bookDTO);
            book.setId(bookId);
            return Book.parseToDtoMono(repository.save(book));
        } else {
            throw new NoSuchElementException("Book ID not found");
        }
    }
    
    @Override
    public List<BookDTO> listAll() {
        return Book.parseToDtoList(repository.findAll());
    }
}
