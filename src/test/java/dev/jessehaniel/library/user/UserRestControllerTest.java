package dev.jessehaniel.library.user;

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

import java.util.NoSuchElementException;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@Tag("lazy")
class UserRestControllerTest {
    
    private static final String URL_USERS = "/users";
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService service;
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for UserDTO POST method")
    void save() throws Exception {
        Mockito.when(service.save(Mockito.any(UserDTO.class))).thenAnswer(i -> i.getArgument(0));
    
        UserDTO userDTO = new UserDTO(1, "user1", 11, "1234");
        String userJson = new ObjectMapper().writeValueAsString(userDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_USERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJson));
        Mockito.verify(service, times(1)).save(userDTO);
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for UserDTO DELETE method")
    void delete() throws Exception {
        int userId = 1;
        String urlDelete = URL_USERS + "/{userId}";
        mockMvc.perform(MockMvcRequestBuilders.delete(urlDelete, userId)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
        Mockito.verify(service, times(1)).delete(userId);
    }
    
    @Test
    @Tag("update")
    @DisplayName("RestController test for UserDTO PUT method")
    void update() throws Exception {
        UserDTO userDTO = new UserDTO(1, "user1", 11, "1234");
        int userId = userDTO.getId();
    
        Mockito.when(service.update(Mockito.anyInt(), Mockito.any(UserDTO.class))).thenReturn(userDTO);
    
        String urlUpdate = URL_USERS + "/{userId}";
        String jsonUser = new ObjectMapper().writeValueAsString(userDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(urlUpdate, userId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUser));
        Mockito.verify(service, times(1)).update(userId, userDTO);
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception Handler test when Id NOT FOUND for PUT method")
    void exceptionHandlerWhenUpdateNotFound() throws Exception {
        UserDTO userDTO = new UserDTO();
        int userId = 5;
        
        Mockito.when(service.update(Mockito.anyInt(), Mockito.any(UserDTO.class))).thenThrow(NoSuchElementException.class);
        
        String urlUpdate = URL_USERS + "/{userId}";
        String jsonUser = new ObjectMapper().writeValueAsString(userDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(urlUpdate, userId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonUser))
                .andExpect(status().isNotFound());
        Mockito.verify(service, times(1)).update(userId, userDTO);
    }
    
    @Test
    @Tag("list")
    @DisplayName("RestController test for UserDTO GET method")
    void listAll() throws Exception {
        mockMvc.perform(get(URL_USERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        Mockito.verify(service, times(1)).listAll();
    }
}
