package org.bluebank.atm.transaction.bdd.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Table;
import org.bluebank.api.domain.Repository;
import org.bluebank.api.endpoint.OutboundEndPoint;
import org.bluebank.atm.Atm;
import org.bluebank.banking.account.model.Account;
import org.bluebank.bdd.BddComponent;
import org.bluebank.bdd.model.Receipt;
import org.bluebank.contract.Messages;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tngtech.jgiven.annotation.Table.HeaderType.NONE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;

public class ThenAtmDispenseMoney<SELF extends ThenAtmDispenseMoney<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    private BddComponent bddComponent;

    @ExpectedScenarioState
    private UUID accountId;

    private ArgumentCaptor<Messages.Receipt> captor;
    private OutboundEndPoint<Messages.Receipt> receiptSender;
    private Atm atm;
    private Repository<Account> accountRepository;

    @BeforeStage
    public void before() {
        receiptSender = bddComponent.getReceiptSender();
        captor = forClass(Messages.Receipt.class);
        atm = bddComponent.getAtm();
        accountRepository = bddComponent.getAccountRepository();
    }

    public SELF the_account_balance_is_$_dollars(String amount) {
        Optional<Account> account = accountRepository.load(accountId);
        assertThat(account.get().getBalance()).isEqualTo(new BigDecimal(amount));
        return self();
    }

    public SELF the_ATM_accepts_$_dollars(String amount) {
        ArgumentCaptor<BigDecimal> captor = forClass(BigDecimal.class);
        verify(atm).addCash(captor.capture());
        BigDecimal cash = captor.getValue();
        assertThat(cash).isEqualTo(new BigDecimal(amount));
        return self();
    }

    public SELF the_ATM_dispenses_$_dollars(String amount) {
        ArgumentCaptor<BigDecimal> captor = forClass(BigDecimal.class);
        verify(atm).dispenseCash(captor.capture());
        BigDecimal cash = captor.getValue();
        assertThat(cash).isEqualTo(new BigDecimal(amount));
        return self();
    }

    public SELF the_card_is_returned() {
        verify(atm).ejectCard();
        return self();
    }

    public SELF a_receipt_is_printed(@Table(header = NONE, columnTitles = "Receipt") Receipt... receipt) {
        verify(receiptSender).send(captor.capture());
        Messages.Receipt receiptDto = captor.getValue();
        List<Receipt> receipts = receiptDto.getDataList().stream()
                .map(Receipt::new)
                .collect(toList());

        assertThat(receipts).containsExactly(receipt);
        return self();
    }
}
