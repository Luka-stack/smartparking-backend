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
import pl.ttpsc.smartparking.error.exception.NotFoundPlateException;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.service.PlateService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.ttpsc.smartparking.controller.AbstractRestControllerTest.asJsonString;

class PlateControllerUT {

    private final String BASE_URL = "/api/plate";
    private final String PLATE_NUM = "FZ12345";

    @Mock
    private PlateService plateService;

    @InjectMocks
    private PlateController plateController;

    @Autowired
    private MockMvc mockMvc;

    private PlateEntity plateEntity;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(plateController)
                .setControllerAdvice(new RestErrorAdvice()).build();

        plateEntity = createPlateEntity(PLATE_NUM);
    }

    @Test
    void shouldCreatePlate() throws Exception {

        // given
        PlateEntity createdPlate = createPlateEntity(PLATE_NUM);

        // when
        when(plateService.createPlate(any())).thenReturn(createdPlate);

        // then
        mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(plateEntity)))
                .andExpect(jsonPath("$.plate", equalTo(PLATE_NUM)))
                .andExpect(status().isOk());
    }

    @Test
    void createShouldThrowInvalidInput() throws Exception {

        // when
        when(plateService.createPlate(any())).thenThrow(InvalidInputException.class);

        // then
        mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(plateEntity)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatePlate() throws Exception {

        // given
        PlateEntity returnedPlate = createPlateEntity(PLATE_NUM);

        // when
        when(plateService.updatePlate(anyLong(), any())).thenReturn(returnedPlate);

        // then
        mockMvc.perform(put(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(plateEntity)))
                .andExpect(jsonPath("$.plate", equalTo(PLATE_NUM)))
                .andExpect(status().isOk());
    }

    @Test
    void updateShouldThrowNotFoundAccessException() throws Exception {
        
        // when
        when(plateService.updatePlate(anyLong(), any())).thenThrow(NotFoundPlateException.class);
        
        // then
        mockMvc.perform(put(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(plateEntity)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldThrowInvalidInput() throws Exception {

        // when
        when(plateService.updatePlate(anyLong(), any())).thenThrow(InvalidInputException.class);

        // then
        mockMvc.perform(put(BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(plateEntity)))
                .andExpect(status().isBadRequest());
    }

    private PlateEntity createPlateEntity(String plate) {

        PlateEntity plateEntity = new PlateEntity();
        plateEntity.setPlate(plate);

        return plateEntity;
    }


}