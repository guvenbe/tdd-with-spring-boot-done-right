package de.rieckpil;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
  public List<Comment> findAll() {
    return List.of(
      new Comment(
        UUID.randomUUID(),
        "1",
        "Hello World",
        LocalDate.now()
      ),
      new Comment(
        UUID.randomUUID(),
        "2",
        "Hello World 2",
        LocalDate.now()
      ),
      new Comment(
        UUID.randomUUID(),
        "3",
        "Hello World 3",
        LocalDate.now()
      )
    );
  }

  public UUID createComment(String content, String authorName) {
    return UUID.randomUUID();
  }
}
