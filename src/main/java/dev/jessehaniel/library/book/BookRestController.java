package dev.jessehaniel.library.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookRestController {
    
    private BookService service;
    
    public BookRestController(BookService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO save(@RequestBody BookDTO book){
        return service.save(book);
    }
    
    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer bookId) {
        service.delete(bookId);
    }
    
    @PutMapping("/{bookId}")
    public BookDTO update(@PathVariable int bookId, @RequestBody BookDTO bookDTO) {
        return service.update(bookId, bookDTO);
    }
    
    @GetMapping
    public List<BookDTO> listAll(){
        return service.listAll();
    }
}
