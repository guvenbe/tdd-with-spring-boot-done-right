package de.rieckpil;

import java.util.Set;

public record Post(
  Long id,
  String title,
  String content,
  long authorId,
  Set<String> tags,
  long reactions
) {
}
