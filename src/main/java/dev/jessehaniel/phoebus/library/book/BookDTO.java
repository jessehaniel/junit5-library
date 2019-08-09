package dev.jessehaniel.phoebus.library.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
class BookDTO implements Serializable {
    private static final long serialVersionUID = -2843839062908896824L;
    private int id;
    private String title;
    private String authorName;
    private String resume;
    private String isbn;
    private int yearRelease;
}