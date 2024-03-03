package de.rieckpil;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Develop an HTTP client that fetch all posts from the post-service and return them as a list. The
 * post-service returns the all posts with pagination. The client should fetch all pages and return
 * the result as a list.
 */
public class PostClient {

  private final WebClient postWebClient;

  public PostClient(WebClient postWebClient) {
    this.postWebClient = postWebClient;
  }

  public List<Post> fetchAllPosts() {

    PostResults result = postWebClient
      .get()
      .uri("/posts?limit={limit}&skip={skip}", 30, 0)
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(PostResults.class)
      .block();

    //now paginate over the result and return final list once page is reached

    return result.posts();
  }
}
