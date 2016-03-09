package org.bluebank.atm.authorization.model;

public class AtmInfo {
    public final String id;
    public final String location;
    public final String bankName;

    public AtmInfo(String id, String location, String bankName) {
        this.id = id;
        this.location = location;
        this.bankName = bankName;
    }
}
