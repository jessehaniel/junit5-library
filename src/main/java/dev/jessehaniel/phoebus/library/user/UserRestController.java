package dev.jessehaniel.phoebus.library.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
class UserRestController {
    
    private UserService service;
    
    UserRestController(UserService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO save(@RequestBody UserDTO userDTO){
        return service.save(userDTO);
    }
    
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer userId) {
        service.delete(userId);
    }
    
    @PutMapping("/{userId}")
    public UserDTO update(@PathVariable int userId, @RequestBody UserDTO userDTO) {
        return service.update(userId, userDTO);
    }
    
    @GetMapping
    public List<UserDTO> listAll(){
        return service.listAll();
    }
}
