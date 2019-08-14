package dev.jessehaniel.phoebus.library.loan;

import dev.jessehaniel.phoebus.library.book.BookDTO;
import dev.jessehaniel.phoebus.library.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
class LoanDTO implements Serializable {
    private static final long serialVersionUID = -2650572420135015345L;
    
    private Integer id;
    private int durationDays;
    private UserDTO user;
    private List<BookDTO> bookList;
}
