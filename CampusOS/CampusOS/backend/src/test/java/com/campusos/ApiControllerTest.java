package com.campusos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {
  @Autowired MockMvc mvc;
  @Test void dashboardReturnsRoleAndMetrics() throws Exception {
    mvc.perform(get("/api/v1/dashboard").param("role","STUDENT"))
      .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("STUDENT"))
      .andExpect(jsonPath("$.resources").exists());
  }
  @Test void knowledgeSearchReturnsList() throws Exception {
    mvc.perform(get("/api/v1/knowledge").param("q","library"))
      .andExpect(status().isOk());
  }
}
