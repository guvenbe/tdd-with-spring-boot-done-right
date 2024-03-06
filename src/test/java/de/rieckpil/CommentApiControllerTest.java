package de.rieckpil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfiguration.class)
@WebMvcTest(CommentApiController.class)
class CommentApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldAllowAnonymousUsersToGetAllComments() throws Exception {
    mockMvc
      .perform(get("/api/comments")
        .header(ACCEPT, APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(jsonPath("$.length()", is(3)))
      .andExpect(jsonPath("$[0].content", notNullValue()))
      .andExpect(jsonPath("$[0].id", notNullValue()))
      .andExpect(jsonPath("$[0].creationDate", notNullValue()))
      .andExpect(jsonPath("$[0].authorId", notNullValue()));

  }

}
