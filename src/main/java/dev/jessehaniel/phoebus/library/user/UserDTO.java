package dev.jessehaniel.phoebus.library.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public
class UserDTO implements Serializable {
    private static final long serialVersionUID = 3304936624683755096L;
    private Integer id;
    private String name;
    private Integer age;
    private String phoneNumber;
}
