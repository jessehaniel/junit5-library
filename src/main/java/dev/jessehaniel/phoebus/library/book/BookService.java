package dev.jessehaniel.phoebus.library.book;

import java.util.List;

interface BookService {
    BookDTO save (BookDTO bookDTO);
    BookDTO delete (int bookId);
    BookDTO update(int bookId, BookDTO bookDTO);
    List<BookDTO> listAll();
}
