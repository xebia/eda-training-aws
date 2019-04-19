package com.xebia.soa.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.common.domain.Claim;
import com.xebia.common.domain.ProductClaim;
import com.xebia.soa.SoaInventoryApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.xebia.common.DomainSamples.createProductClaims;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {SoaInventoryApplication.class})
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.soa.*" })
@ActiveProfiles({"default", "test"})
public class SoaInventoryControllerTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldClaimInventory() throws Exception {
        List<ProductClaim> orderLines = createProductClaims(3);
        MvcResult response = mockMvc.perform(post("/inventory-api/v1/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderLines))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Claim inserted = mapper.readValue(response.getResponse().getContentAsString(), Claim.class);
        assertEquals(inserted.getClaims().size(), 3);
    }

}
