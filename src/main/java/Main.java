import dto.Match;
import dto.MatchList;
import dto.Summoner;
import service.ConfigurationService;
import service.MatchService;
import service.SummonerService;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

public class Main {

    static ConfigurationService config;
    static SummonerService summonerService;
    static MatchService matchService;

    /**
     *
     * @param args API Key, Summoner name, and that summoner's realm
     */
    public static void main(String[] args) {
        config = new ConfigurationService(args[0]);
        summonerService = new SummonerService(config);
        matchService = new MatchService(config);
        Summoner summoner = summonerService.findSummoner(args[1], args[2]);
        MatchList matches = matchService.getMatchList(summoner.getSummonerId());
        ArrayList<Long> champions = new ArrayList<>();

        Map map = new HashMap();

        int twitchCount = 0;

        for (Match match : matches.getMatches()){
            long championId = match.getChampion();
            if (championId == 29){
                twitchCount++;
            }
        }

        if (twitchCount > 0){
            map.put("tilted", false);
            System.out.println(summoner.getSummonerName() + " is not tilted lately...");
            System.out.println("... because they played Twitch recently!");
            if (twitchCount > 1){
                System.out.println(twitchCount + " times in fact!");
            }
        } else{
            map.put("tilted", true);
        }
        map.put("twitchGames", twitchCount);

        get("/tilt", (req, res) -> {
            return new ModelAndView(map, "tilt.mustache");
        }, new MustacheTemplateEngine());

        get("/hello", (req, res) -> "Hello World");


    }



}