package co.edu.javeriana.security.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello(){
        ObjectNode json = new ObjectNode(new JsonNodeFactory(false));
        json.put("nombre", "Pepito");
        return json.toPrettyString();
    }

}
