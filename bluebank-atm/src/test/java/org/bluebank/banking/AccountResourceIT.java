package org.bluebank.banking;


import org.bluebank.api.domain.IdGenerator;
import org.bluebank.api.domain.Repository;
import org.bluebank.banking.account.model.Account;
import org.bluebank.resource.AccountResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountResourceIT extends JerseyTest {

    @Override
    protected Application configure() {
        Account account = new Account(new UUID(0, 1), new IdGenerator());
        Repository<Account> repository = mock(Repository.class);
        when(repository.load(new UUID(0, 1))).thenReturn(Optional.of(account));
        when(repository.load(new UUID(0, 2))).thenReturn(Optional.empty());
        AccountResource accountResource = new AccountResource(repository);
        return new ResourceConfig().register(accountResource);
    }

    @Test
    public void should_get_all_accounts() {
        Response response = target("accounts").request(APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void should_get_account(){
        Response response = target("accounts").path(new UUID(0, 1).toString()).request(APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void should_not_get_account(){
        Response response = target("accounts").path(new UUID(0, 2).toString()).request(APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }
}
