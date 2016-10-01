package dto;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class Summoner {

    long summonerId;
    String summonerName;
    int summonerLevel;

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public long getSummonerId(){ return summonerId; }

    public String getSummonerName(){ return summonerName; }

    private int getSummonerLevel(){ return summonerLevel; }

    public String toString(){
        StringBuilder sb = new StringBuilder()
        .append(summonerName)
                .append(": ")
                .append("a summoner of level ")
                .append(summonerLevel);
        return sb.toString();
    }
}
