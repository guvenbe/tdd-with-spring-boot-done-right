package de.rieckpil;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostClientTest {

  @RegisterExtension
  static WireMockExtension mockServer =
    WireMockExtension.newInstance().options(wireMockConfig().dynamicHttpsPort()).build();

  private PostClient cut = new PostClient(
    WebClient.builder().baseUrl(mockServer.baseUrl()).build());

  @Test
  void shouldReturnAllPosts() throws Exception {
    mockServer.stubFor(WireMock.get("/posts")
      .withQueryParam("limit", WireMock.equalTo("30")) //?limt=30&skip=0
      .withQueryParam("limit", WireMock.equalTo("0"))
      .willReturn(WireMock.aResponse()
        .withBodyFile("dummyjson/get-all-posts-page-one.json")
        .withHeader("Content-Type", "application/json")));

    List<Post> posts = cut.fetchAllPosts();
    assertThat(posts).hasSize(90);
  }

}
