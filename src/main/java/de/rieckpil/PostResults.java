package de.rieckpil;

import java.util.List;

public record PostResults(List<Post> posts,
                          long total,
                          long skip, long limit) {
}
