package com.github.mehmetsahinnn.onlineordertrackingsystem.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.Response;
import java.util.List;

@Component
public class KeycloakClient {

    private final Keycloak keycloak;

    public KeycloakClient() {
        this.keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:8180")
                .realm("order-tracking-system")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("admin-cli")
                .clientSecret("6y3UmBX97w52msI1ZzMcHXZPqpQoUtrA")

                .build();
}

    public void createUser(String username, String password, String firstName, String lastName) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(username);
        user.setFirstName(firstName);  // add this line
        user.setLastName(lastName);    // add this line

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(List.of(credential));

        Response response = keycloak.realm("order-tracking-system").users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user");
        }
    }

    public String getLoginToken(String username, String password) {
        AccessTokenResponse response = keycloak.tokenManager().getAccessToken();
        return response.getToken();
    }
}