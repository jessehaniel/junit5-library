package dev.jessehaniel.phoebus.library.book;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {
    
    private BookRepository repository;
    
    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public BookDTO save(BookDTO bookDTO) {
        Book book = Book.of(bookDTO);
        book.setId(null);
        return Book.parseToDtoMono(repository.save(book));
    }
    
    @Override
    public BookDTO delete(int bookId) {
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book ID not found"));
        repository.delete(book);
        return Book.parseToDtoMono(book);
    }
    
    @Override
    public BookDTO update(int bookId, BookDTO bookDTO) {
        if (repository.findById(bookId).isPresent()) {
            return Book.parseToDtoMono(repository.save(Book.of(bookDTO)));
        } else {
            throw new NoSuchElementException("Book ID not found");
        }
    }
    
    @Override
    public List<BookDTO> listAll() {
        return Book.parseToDtoList(repository.findAll());
    }
}
