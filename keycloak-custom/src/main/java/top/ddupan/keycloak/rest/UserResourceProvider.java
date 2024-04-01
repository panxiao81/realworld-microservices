package top.ddupan.keycloak.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

public class UserResourceProvider implements RealmResourceProvider {
    private final KeycloakSession session;

    public UserResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @GET
    @Path("/user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel getUserByUsername(@PathParam("username") String username) {
        RealmModel realm = session.getContext().getRealm();
        UserModel user = session.users().getUserByUsername(realm, username);
        return user;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
