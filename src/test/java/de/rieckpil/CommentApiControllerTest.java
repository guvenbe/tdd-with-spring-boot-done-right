package de.rieckpil;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfiguration.class)
@WebMvcTest(CommentApiController.class)
class CommentApiControllerTest {

  @MockBean
  private CommentService commentService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldAllowAnonymousUsersToGetAllComments() throws Exception {

    when(commentService.findAll()).thenReturn(List.of(
      new Comment(
        UUID.randomUUID(),
        "40",
        "Lorem Ipsum",
        LocalDate.now()
      ),
      new Comment(
        UUID.randomUUID(),
        "41",
        "Lorem Ipsum",
        LocalDate.now().minusDays(1)
      ),
      new Comment(
        UUID.randomUUID(),
        "42",
        "Lorem Ipsum",
        LocalDate.now().minusDays(2)
      )
    ));

    mockMvc
      .perform(get("/api/comments")
        .header(ACCEPT, APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(jsonPath("$.length()", is(3)))
      .andExpect(jsonPath("$[0].content", notNullValue()))
      .andExpect(jsonPath("$[0].id", notNullValue()))
      .andExpect(jsonPath("$[0].creationDate", notNullValue()))
      .andExpect(jsonPath("$[0].authorId", notNullValue()))
      .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

  }

  @Test
  void shouldRejectAnonymousUserWhenCreatingComments() throws Exception {
    mockMvc
      .perform(post("/api/comments")
        .header(ACCEPT, APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .content("""
          {
            "content": "Lorem Ipsum"
          }
          """))
      .andExpect(status().isUnauthorized()); // 401  UNAUTHORIZED
  }

  @Test
  @WithMockUser(username = "duke", roles = {"VISITOR"})
  void shouldRejectAuthenticatedUserWithoutAdminRoleWhenCreatingComments() throws Exception {
    mockMvc
      .perform(post("/api/comments")
        .header(ACCEPT, APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .content("""
          {
            "content": "Lorem Ipsum"
          }
          """))
      .andExpect(status().isForbidden()); // 403  FORBIDDEN
  }

  @Test
  @WithMockUser(username = "duke", roles = {"ADMIN", "VISITOR"})
  void shouldFailOnInvalidCommentData() throws Exception {
    mockMvc
      .perform(post("/api/comments")
        .header(ACCEPT, APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .content("""
          {
            "content": ""
          }
          """))
      .andExpect(status().isBadRequest()); // 400  BADREQUEST
  }

  @Test
  @WithMockUser(username = "duke", roles = {"ADMIN"})
  void shouldCreateCommentWhenUserIsAuthenticatedAndAdmin() throws Exception {
    UUID newlyCreatedCommentId = UUID.randomUUID();
    when(commentService.createComment(anyString(), anyString()))
      .thenReturn(newlyCreatedCommentId);

    mockMvc
      .perform(post("/api/comments")
        .header(ACCEPT, APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .content("""
          {
            "content": "Lorem Ipsum"
          }
          """))
      .andExpect(status().isCreated()) // 201 CREATED
      .andExpect(header().string("Location",
        Matchers.containsString("/api/comments/" + newlyCreatedCommentId.toString())));
  }
}


