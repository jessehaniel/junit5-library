package dev.jessehaniel.phoebus.library.book;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookRestController {
    
    private BookServiceImpl service;
    
    public BookRestController(BookServiceImpl service) {
        this.service = service;
    }
    
    @PostMapping
    public BookDTO save(@RequestBody BookDTO book){
        return service.save(book);
    }
    
    @DeleteMapping("/{bookId}")
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
