package dto;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class Summoner {

    long accountId;
    long summonerId;
    Account currentUser;
    String platformId;
    int summonerLevel;
    String summonerName;

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Account currentUser) {
        this.currentUser = currentUser;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder()
        .append(summonerName)
                .append(": ")
                .append("a summoner of level ")
                .append(summonerLevel);
        return sb.toString();
    }
}
