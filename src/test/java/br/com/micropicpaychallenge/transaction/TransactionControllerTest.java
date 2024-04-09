package br.com.micropicpaychallenge.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void testGetAllTransactions() throws Exception{
        when(transactionService.list()).thenReturn(List.of(new Transaction(1L, 1L, 1L, BigDecimal.valueOf(100), LocalDateTime.now())));
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transaction"))
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateTransaction() throws Exception{
        Transaction request = new Transaction(1L, 1L, 1L, BigDecimal.valueOf(100), LocalDateTime.now());
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(request);
        mockMvc.perform(
            MockMvcRequestBuilders.post("/transaction")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
