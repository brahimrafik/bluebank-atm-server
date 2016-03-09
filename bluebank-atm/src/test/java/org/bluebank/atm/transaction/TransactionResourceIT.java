package org.bluebank.atm.transaction;

import org.bluebank.api.domain.Repository;
import org.bluebank.atm.Transaction;
import org.bluebank.resource.TransactionResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TransactionResourceIT extends JerseyTest {

    @Override
    protected Application configure() {
        Repository<Transaction> repository = mock(Repository.class);
        TransactionResource transactionResource = new TransactionResource(repository);
        return new ResourceConfig().register(transactionResource);
    }

    @Test
    public void should_get_all_transactions() {
        Response response = target("transactions").request(APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }
}
