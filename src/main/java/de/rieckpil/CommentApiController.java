package de.rieckpil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Develop a REST API to retrieve and create comments. Everybody should be able to retrieve comments
 * but only logged-in users with the role ADMIN can create a comment.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentApiController {

  private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
  public List<Comment> getAllComments() {
    return List.of();
  }
}
