package dev.jessehaniel.library.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;

@WebMvcTest(BookRestController.class)
@Tag("lazy")
class BookRestControllerTest {
    
    private static final String URL_BOOKS = "/books";
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for BookDTO POST method")
    void save() throws Exception {
        Mockito.when(bookService.save(Mockito.any(BookDTO.class))).thenAnswer(i -> i.getArgument(0));
    
        BookDTO bookDTO = new BookDTO(1, "book1", "author1", "resume book1", "123", 2019);
        String bookJson = new ObjectMapper().writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_BOOKS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(bookJson));
        Mockito.verify(bookService, Mockito.times(1)).save(bookDTO);
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for BookDTO DELETE method")
    void delete() throws Exception {
        int bookId = 1;
        String urlDelete = URL_BOOKS + "/{bookId}";
        mockMvc.perform(MockMvcRequestBuilders.delete(urlDelete, bookId)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(bookService, Mockito.times(1)).delete(bookId);
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for BookDTO PUT method")
    void update() throws Exception {
        BookDTO bookDTO = new BookDTO(1, "book1", "author1", "resume book1", "123", 2019);
        int bookId = bookDTO.getId();
        
        Mockito.when(bookService.update(Mockito.anyInt(), Mockito.any(BookDTO.class))).thenReturn(bookDTO);
        
        String urlUpdate = URL_BOOKS + "/{bookId}";
        String jsonBook = new ObjectMapper().writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(urlUpdate, bookId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBook))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonBook));
        Mockito.verify(bookService, Mockito.times(1)).update(bookId, bookDTO);
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception Handler test when Id NOT FOUND for PUT method")
    void exceptionHandlerWhenUpdateNotFound() throws Exception {
        BookDTO bookDTO = new BookDTO();
        int bookId = 5;
        
        Mockito.when(bookService.update(Mockito.anyInt(), Mockito.any(BookDTO.class))).thenThrow(NoSuchElementException.class);
        
        String urlUpdate = URL_BOOKS + "/{bookId}";
        String jsonBook = new ObjectMapper().writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(urlUpdate, bookId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonBook))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito.verify(bookService, Mockito.times(1)).update(bookId, bookDTO);
    }
    
    @Test
    @Tag("list")
    @DisplayName("RestController test for BookDTO GET method")
    void listAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BOOKS)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
        Mockito.verify(bookService, Mockito.times(1)).listAll();
    }
}
