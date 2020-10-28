package dev.jessehaniel.library.loan;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
class LoanServiceImpl implements LoanService {
    
    private LoanRepository repository;
    
    LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        Loan loan = Loan.of(loanDTO);
        loan.setId(null);
        return Loan.parseToDtoMono(repository.save(loan));
    }
    
    @Override
    public void delete(int loanId) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(NoSuchElementException::new);
        repository.delete(loan);
    }
    
    @Override
    public LoanDTO update(int loanId, LoanDTO loanDTO) {
        if (repository.findById(loanId).isPresent()) {
            Loan loan = Loan.of(loanDTO);
            loan.setId(loanId);
            return Loan.parseToDtoMono(repository.save(loan));
        } else {
            throw new NoSuchElementException("Loan ID not found");
        }
    }
    
    @Override
    public List<LoanDTO> listAll() {
        return Loan.parseToDtoList(repository.findAll());
    }
}
