package org.bluebank.resource;

import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Singleton
@Path("/transactions")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class TransactionResource {
    private final Repository<Transaction> repository;

    @Inject
    public TransactionResource(Repository<Transaction> repository) {
        this.repository = repository;
    }

    @GET
    public Collection<Transaction> getTransactions() {
        return repository.all();
    }

}
