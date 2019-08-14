package dev.jessehaniel.phoebus.library.loan;

import dev.jessehaniel.phoebus.library.book.BookDTO;
import dev.jessehaniel.phoebus.library.user.UserDTO;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class LoanServiceTest {
    
    @Mock
    private LoanRepository repository;
    private LoanService service;
    private List<LoanDTO> loanDTOListMock;
    
    @BeforeEach
    void setUp() {
        service = new LoanServiceImpl(repository);
        loanDTOListMock = Arrays.asList(
                new LoanDTO(1, 1, new UserDTO(1, "user1", 1, "123"), Collections.singletonList(new BookDTO(1, "book1",
                        "author1", "resume book1", "123", 2019))),
                new LoanDTO(2, 2, new UserDTO(1, "user1", 1, "123"), Arrays.asList(new BookDTO(1, "book1",
                        "author1", "resume book1", "123", 2019), new BookDTO(1, "book1",
                        "author1", "resume book1", "123", 2019))),
                new LoanDTO(3, 3, new UserDTO(1, "user1", 1, "123"), Collections.singletonList(new BookDTO(1, "book1",
                        "author1", "resume book1", "123", 2019)))
        );
    }
    
    @Test
    @Tag("update")
    void save() {
        when(repository.save(Mockito.any(Loan.class))).thenAnswer(i -> {
           Loan loan = i.getArgument(0);
           loan.setId(loanDTOListMock.size() + 1);
           return loan;
        });
        LoanDTO loanDTO = loanDTOListMock.stream().findAny().orElseGet(LoanDTO::new);
        LoanDTO loanSaved = service.save(loanDTO);
    
        verify(repository, times(1)).save(Mockito.any(Loan.class));
        assertThat(loanSaved, CoreMatchers.notNullValue());
        assertThat(loanSaved.getId(), CoreMatchers.notNullValue());
    }
    
    @Test
    @Tag("update")
    void delete() {
        Optional<Loan> loan = loanDTOListMock.stream().map(Loan::of).findAny();
        when(repository.findById(anyInt())).thenReturn(loan);
    
        int loanId = 0;
        service.delete(loanId);
    
        verify(repository, times(1)).findById(loanId);
        verify(repository, times(1)).delete(loan.orElseThrow(NoSuchElementException::new));
    }
    
    @Test
    @Tag("update")
    void update() {
        Optional<Loan> loan = loanDTOListMock.stream().map(Loan::of).findAny();
        when(repository.findById(anyInt())).thenReturn(loan);
        when(repository.save(Mockito.any(Loan.class))).thenAnswer(i -> i.getArgument(0));
    
        LoanDTO loanDTO = Loan.parseToDtoMono(loan.orElseGet(Loan::new));
        int loanId = loanDTO.getId();
        loanDTO.setDurationDays(loanDTO.getDurationDays()+1);
        LoanDTO loanUpdated = service.update(loanId, loanDTO);
    
        verify(repository, times(1)).findById(loanId);
        verify(repository, times(1)).save(Loan.of(loanDTO));
        assertThat(loanUpdated.getId(), equalTo(loanDTO.getId()));
        assertThat(loanUpdated.getDurationDays(), greaterThan(Loan.parseToDtoMono(loan.orElseGet(Loan::new)).getDurationDays()));
    }
    
    @Test
    @Tag("exception")
    @DisplayName("Exception throws assertion on UPDATE when Loan id not found")
    void updateWithNotFoundException(){
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        
        LoanDTO loanDTO = loanDTOListMock.stream().findAny().orElseGet(LoanDTO::new);
    
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.update(loanDTO.getId(), loanDTO));
        assertEquals("Loan ID not found", exception.getMessage());
    }
    
    @Test
    @Tag("list")
    void listAll() {
        List<Loan> loanList = loanDTOListMock.stream().map(Loan::of).collect(Collectors.toList());
        when(repository.findAll()).thenReturn(loanList);
        
        List<LoanDTO> loanDTOList = service.listAll();
        
        verify(repository, times(1)).findAll();
        assertThat(loanDTOList, CoreMatchers.is(loanDTOListMock));
    }
}