package dto;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class Account {
    long accountId;
    String platformId;

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public long getAccountId() {
        return accountId;
    }


}
