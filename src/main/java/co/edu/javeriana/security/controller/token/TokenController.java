package co.edu.javeriana.security.controller.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {


    @Autowired
    JwtEncoder encoder;

    @Autowired
    JwtDecoder decoder;

    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public String token(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 60L;
        // @formatter:off
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        // @formatter:on
        ObjectNode json = new ObjectNode(new JsonNodeFactory(false));
        json.put("token",this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
        return json.toPrettyString();
    }

    @GetMapping("/validate")
    public ResponseEntity validateFilter(@RequestHeader(value = "Authorization")String jwtToken){
        Jwt token = decoder.decode(jwtToken.split(" ")[1]);
        if(token.getExpiresAt().isAfter(Instant.now())) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
