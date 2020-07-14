package pl.ttpsc.smartparking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.ttpsc.smartparking.error.RestErrorAdvice;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.service.AccessService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.ttpsc.smartparking.controller.AbstractRestControllerTest.asJsonString;

public class AccessControllerUT {

    private final String BASE_URL = "/api/access";

    @Mock
    private AccessService accessService;

    @InjectMocks
    private AccessController accessController;

    private AccessEntity accessEntity;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accessController)
                .setControllerAdvice(new RestErrorAdvice()).build();

        accessEntity = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    void shouldUpdateAccess() throws Exception {

        // given
        AccessEntity returnedAccessEntity = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(1));

        // when
        when(accessService.update(anyLong(), any())).thenReturn(returnedAccessEntity);

        // then
        mockMvc.perform(put(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessEntity)))
                .andExpect(jsonPath("$.dateFrom", equalTo(LocalDate.now().toString())))
                .andExpect(jsonPath("$.dateTo", equalTo(LocalDate.now().plusDays(1).toString())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowNotFoundAccessException() throws Exception {

        // when
        when(accessService.update(anyLong(), any())).thenThrow(NotFoundAccessException.class);

        // then
        mockMvc.perform(put(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessEntity)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowInvalidInput() throws Exception {

       // when
       when(accessService.update(anyLong(), any())).thenThrow(InvalidInputException.class);

       // then
        mockMvc.perform(put(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessEntity)))
                .andExpect(status().isBadRequest());
    }

    private AccessEntity createAccessEntity(LocalDate dateFrom, LocalDate dateTo) {

        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setDateFrom(dateFrom);
        accessEntity.setDateTo(dateTo);

        return accessEntity;
    }
}
