package dev.chan.drive.app.item;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.chan.drive.app.storage.PresignedUpload;
import dev.chan.drive.security.WithMockLoginUser;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

  @Autowired MockMvc mockMvc;
  @MockitoBean CreateItemService createItemService;
  private static final String VALID_BODY =
      """
      {"parentId": 10, "mimeType": "text/plain", "fileName": "test.txt", "fileSize": 1024}
      """;

  @Test
  @WithMockLoginUser(id = 24L)
  public void 파일_생성_성공시_업로드_정보를_반환하고_로그인한_유저로_서비스를_호출한다() throws Exception {

    // given
    final DriveItem file = DriveItemFixture.file(42L, "file", 24L);

    final PresignedUpload upload =
        new PresignedUpload(
            URI.create("http://localhost:8080/upload"),
            Map.of("Content-Type", "text/plain"),
            Instant.parse("2026-11-24T00:00:00Z"));

    given(createItemService.create(eq(24L), any())) // userId까지 스텁 조건에 포함
        .willReturn(new CreateItemService.Output(file, upload));

    // when & then
    mockMvc
        .perform(post("/api/v1/items").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.itemId").value(1))
        .andExpect(jsonPath("$.upload.url").value("http://localhost:8080/upload"))
        .andExpect(jsonPath("$.upload.requiredHeaders['Content-Type']").value("text/plain"));
  }

  /**
   * 검증 값
   * - mimeType이 null 또는 ""인 경우
   * - fileSize가 음수인 경우
   * - fileName이 null 또는 ""인 경우
   *
   * @param body
   * @throws Exception
   */
  @ParameterizedTest
  @ValueSource(
      strings = {
        """
           {"parentId": 10, "mimeType": "text/plain", "fileName": "test.txt", "fileSize": -1}
        """,
        """
          {"parentId": 10, "mimeType": "text/plain", "fileName": "", "fileSize": 10}
        """,
        """
          {"parentId": 10, "mimeType": "", "fileName": "", "fileSize": 10}
        """,
        """
          {"parentId": 10, "mimeType": "text/plain", "fileName": null, "fileSize": 10}
        """,
        """
          {"parentId": null, "mimeType": null, "fileName": "text.txt", "fileSize": 10}
        """,
      })
  @WithMockLoginUser(id = 24L)
  void 요청값이_올바르지_않으면_400이고_서비스를_호출하지_않는다(final String body) throws Exception {
    mockMvc
        .perform(post("/api/v1/items").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(createItemService);
  }

  @Test
  @WithMockLoginUser(id = 24L)
  void 요청_본문이_없으면_400이고_서비스를_호출하지_않는다() throws Exception {
    mockMvc
        .perform(post("/api/v1/items").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(createItemService);
  }
}
