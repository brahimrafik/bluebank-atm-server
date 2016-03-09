package org.bluebank.resource;

import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;

import static java.util.UUID.fromString;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ResponseBuilder;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Singleton
@Path("/accounts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AccountResource {
    private final Repository<Account> repository;

    @Inject
    public AccountResource(Repository<Account> repository) {
        this.repository = repository;
    }

    @GET
    public Collection<Account> getTransactions() {
        return repository.all();
    }

    @GET
    @Path("{id}")
    public Response getAccount(@PathParam("id") String id) {
        ResponseBuilder response;
        Optional<Account> account = repository.load(fromString(id));
        if (account.isPresent()) {
            response = ok(account.get());
        } else {
            response = status(BAD_REQUEST);
        }
        return response.build();
    }
}
