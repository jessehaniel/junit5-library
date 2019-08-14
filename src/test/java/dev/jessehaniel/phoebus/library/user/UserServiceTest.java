package dev.jessehaniel.phoebus.library.user;

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
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for USER Service - CRUD operations")
@Tag("fast")
class UserServiceTest {
    
    @Mock
    private UserRepository repository;
    private UserService service;
    private List<UserDTO> userDTOList;
    
    @BeforeEach
    void setUp() {
        this.service = new UserServiceImpl(repository);
        this.userDTOList = Arrays.asList(
                new UserDTO(1, "user1", 1, "123"),
                new UserDTO(2, "user2", 2, "456"),
                new UserDTO(3, "user3", 3, "123456")
        );
    }
    
    @Test
    void save() {
        when(repository.save(Mockito.any(User.class))).thenAnswer(i -> {
            User user = i.getArgument(0);
            user.setId(userDTOList.size() + 1);
            return user;
        });
        
        UserDTO userDTO = new UserDTO(5, "user5", 11, "123");
        UserDTO userDtoSaved = service.save(userDTO);
        
        verify(repository, times(1)).save(Mockito.any(User.class));
        assertThat(userDtoSaved, CoreMatchers.notNullValue());
        assertThat(userDtoSaved.getId(), CoreMatchers.notNullValue());
    }
    
    @Test
    void delete() {
        Optional<User> user = userDTOList.stream().map(User::of).findAny();
        when(repository.findById(Mockito.anyInt())).thenReturn(user);
        
        int userId = 0;
        service.delete(userId);
        
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).delete(user.orElseThrow(NoSuchElementException::new));
    }
    
    @Test
    void update() {
        Optional<User> user = userDTOList.stream().map(User::of).findAny();
        when(repository.findById(Mockito.anyInt())).thenReturn(user);
        when(repository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));
        
        int userId = 0;
        UserDTO userDto = User.parseToDtoMono(user.orElse(new User()));
        userDto.setName(userDto.getName().toUpperCase());
        UserDTO userUpdated = service.update(userId, userDto);
        
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).save(User.of(userDto));
        assertThat(userUpdated.getId(), equalTo(userDto.getId()));
        assertThat(userUpdated.getName(), equalTo(userDto.getName().toUpperCase()));
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception throws assertion on UPDATE when User id not found")
    void updateWithNotFoundException() {
        when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        UserDTO userDTO = new UserDTO(1, "user1", 11, "1234");
        
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.update(userDTO.getId(), userDTO));
        assertEquals("User ID not found", exception.getMessage());
    }
    
    @Test
    void listAll() {
        List<User> userList = userDTOList.stream().map(User::of).collect(Collectors.toList());
        when(repository.findAll()).thenReturn(userList);
        
        List<UserDTO> returnList = service.listAll();
        
        assertThat(returnList, CoreMatchers.is(userDTOList));
        verify(repository, times(1)).findAll();
    }
}