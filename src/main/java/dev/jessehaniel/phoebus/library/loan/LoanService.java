package dev.jessehaniel.phoebus.library.loan;

import java.util.List;

interface LoanService {
    LoanDTO save(LoanDTO loan);
    void delete(int loanId);
    LoanDTO update(int loanId, LoanDTO loan);
    List<LoanDTO> listAll();
}
