package dev.jessehaniel.library.loan;

import dev.jessehaniel.library.book.Book;
import dev.jessehaniel.library.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int durationDays;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(name = "loan_books",
            joinColumns = {@JoinColumn(name = "loan_id")}, inverseJoinColumns = {@JoinColumn(name = "book_id")})
    private List<Book> bookList;
    
    static Loan of(LoanDTO loanDTO) {
        return new Loan(loanDTO.getId(), loanDTO.getDurationDays(),
                User.of(loanDTO.getUser()),
                Book.ofList(loanDTO.getBookList()));
    }
    
    static LoanDTO parseToDtoMono(Loan loan){
        return new LoanDTO(loan.getId(), loan.getDurationDays(),
                User.parseToDtoMono(loan.getUser()),
                Book.parseToDtoList(loan.getBookList()));
    }
    
    static List<LoanDTO> parseToDtoList(List<Loan> loanList) {
        return loanList.stream().map(Loan::parseToDtoMono).collect(Collectors.toList());
    }
}
