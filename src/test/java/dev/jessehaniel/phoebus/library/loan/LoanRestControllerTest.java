package dev.jessehaniel.phoebus.library.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jessehaniel.phoebus.library.book.BookDTO;
import dev.jessehaniel.phoebus.library.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanRestController.class)
@Tag("lazy")
class LoanRestControllerTest {
    
    private static final String URL_LOANS = "/loans";
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LoanService service;
    private LoanDTO loanDTO;
    
    @BeforeEach
    void setUp() {
        loanDTO = new LoanDTO(1, 1, new UserDTO(1, "user1", 1, "123"),
                Collections.singletonList(new BookDTO(1, "book1",
                "author1", "resume book1", "123", 2019)));
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for LoanDTO POST method")
    void save() throws Exception {
        when(service.save(any(LoanDTO.class))).thenAnswer(i -> i.getArgument(0));
        
        String loanJson = new ObjectMapper().writeValueAsString(loanDTO);
        mockMvc.perform(post(URL_LOANS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(loanJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(loanJson));
        verify(service, times(1)).save(loanDTO);
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for LoanDTO DELETE method")
    void delete() throws Exception {
        int loanId = 1;
        String urlDelete = URL_LOANS + "/{loanId}";
        mockMvc.perform(MockMvcRequestBuilders.delete(urlDelete, loanId)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
        verify(service, times(1)).delete(loanId);
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for LoanDTO PUT method")
    void update() throws Exception {
        when(service.update(Mockito.anyInt(), any(LoanDTO.class))).thenReturn(loanDTO);
    
        String urlUpdate = URL_LOANS + "/{loanId}";
        String loanJson = new ObjectMapper().writeValueAsString(loanDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(urlUpdate, loanDTO.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(loanJson))
                .andExpect(status().isOk())
                .andExpect(content().json(loanJson));
        verify(service, times(1)).update(loanDTO.getId(), loanDTO);
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception Handler test when Id NOT FOUND for PUT method")
    void exceptionHandlerWhenUpdateNotFound() throws Exception {
        when(service.update(Mockito.anyInt(), any(LoanDTO.class))).thenThrow(NoSuchElementException.class);
    
        String urlUpdate = URL_LOANS + "/{loanId}";
        String loanJson = new ObjectMapper().writeValueAsString(loanDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(urlUpdate, loanDTO.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(loanJson))
                .andExpect(status().isNotFound());
        Mockito.verify(service, times(1)).update(loanDTO.getId(), loanDTO);
    }
    
    @Test
    @Tag("list")
    @DisplayName("RestController test for LoanDTO GET method")
    void listAll() throws Exception {
        mockMvc.perform(get(URL_LOANS)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(service, times(1)).listAll();
    }
}