package dev.jessehaniel.phoebus.library.loan;

import dev.jessehaniel.phoebus.library.book.Book;
import dev.jessehaniel.phoebus.library.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int durationDays;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    private List<Book> bookList;
}
