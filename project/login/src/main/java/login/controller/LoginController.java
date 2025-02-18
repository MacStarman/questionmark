package login.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import login.model.loginBody;
import login.service.impl.loginServiceImpl;
import service.core.AccountInfo;

@RestController
public class LoginController {


    @Autowired
    private loginServiceImpl service;

    @Value("${account.service.uri}")
    private String databaseURI;

    @Value("${server.port}")
    private int port;

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<loginBody> login(@RequestBody loginBody entered)
            throws JsonMappingException, JsonProcessingException {

        if (service.checkPassword(entered)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(entered);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

    }

    @GetMapping(value = "/test", produces = "application/json")
    public ResponseEntity<List<loginBody>> getAllLogin() {
        List<loginBody> list = service.getAllLoginInfo();
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<AccountInfo> registerAccount(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(json);

       loginBody loginBody = new loginBody(node.get("email").asText(),node.get("password").asText());
       AccountInfo accountInfo = new AccountInfo(node.get("firstName").asText(),node.get("lastName").asText(),node.get("email").asText());
    
       
        service.registerAccount(loginBody);
        
        RestTemplate template = new RestTemplate();

        ResponseEntity<AccountInfo> response = template.postForEntity(databaseURI + "/accounts",accountInfo,AccountInfo.class);
      
        String url = "http://" + databaseURI+ "/accounts/"
                + response.getBody().accountId;
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", url)
                .header("Content-Location", url)
                .body(accountInfo);
    }
    
    
}


