package br.com.micropicpaychallenge.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.micropicpaychallenge.transaction.Transaction;

@Service
public class AuthorizeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeService.class);

    private RestClient restClient;

    public AuthorizeService(RestClient.Builder builder){
        this.restClient = builder
        .baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc")
        .build();
    }

    public void authorize(Transaction transaction){
        LOGGER.info("Authorizing transaction: {}", transaction);
        // var response = restClient.get()
        // .retrieve()
        // .toEntity(Authorization.class);

        var response = new ResponseEntity<Authorization>(new Authorization("Autorizado"), null, 200);
        
        if(response.getStatusCode().isError() || !response.getBody().isAuthorized()
        ) {
            throw new UnauthorizedException("Unauthorized transaction!");
        }
        LOGGER.info("Transaction authorized: {}", transaction);

    }
}
