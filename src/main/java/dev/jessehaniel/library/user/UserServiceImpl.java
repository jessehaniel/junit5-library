package dev.jessehaniel.library.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
class UserServiceImpl implements UserService {
    
    private UserRepository repository;
    
    UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = User.of(userDTO);
        user.setId(null);
        return User.parseToDtoMono(repository.save(user));
    }
    
    @Override
    public void delete(int userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID not found"));
        repository.delete(user);
    }
    
    @Override
    public UserDTO update(int userId, UserDTO userDto) {
        if(repository.findById(userId).isPresent()) {
            userDto.setId(userId);
            User user = User.of(userDto);
            return User.parseToDtoMono(repository.save(user));
        } else {
            throw new NoSuchElementException("User ID not found");
        }
    }
    
    @Override
    public List<UserDTO> listAll() {
        return User.parseToDtoList(repository.findAll());
    }
}
