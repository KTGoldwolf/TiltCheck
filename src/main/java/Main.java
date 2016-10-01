import dto.Match;
import dto.MatchList;
import dto.Summoner;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import service.ConfigurationService;
import service.MatchService;
import service.SummonerService;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    static ConfigurationService config;
    static SummonerService summonerService;
    static MatchService matchService;

    /**
     *
     * @param args API Key
     */
    public static void main(String[] args) {
        staticFileLocation("/public");

        String key = getHerokuSecret();

        if (key == null) {
            key = args[0];
        }

        config = new ConfigurationService(key);
        summonerService = new SummonerService(config);
        matchService = new MatchService(config);



//        Summoner summoner = summonerService.findSummoner(args[1], args[2]);
//        MatchList matches = matchService.getMatchList(summoner.getSummonerId());
//        ArrayList<Long> champions = new ArrayList<>();
//
        Map map = new HashMap();
//
//        int twitchCount = 0;
//
//        for (Match match : matches.getMatches()){
//            long championId = match.getChampion();
//            if (championId == 29){
//                twitchCount++;
//            }
//        }
//
//        if (twitchCount > 0){
//            map.put("tilted", false);
//            System.out.println(summoner.getSummonerName() + " is not tilted lately...");
//            System.out.println("... because they played Twitch recently!");
//            if (twitchCount > 1){
//                System.out.println(twitchCount + " times in fact!");
//            }
//        } else{
//            map.put("tilted", true);
//        }
        int twitchCount = 0;
        map.put("tilted", true);
        map.put("twitchGames", twitchCount);

        port(getHerokuAssignedPort());

        get("/tilt", (req, res) -> {
            return new ModelAndView(map, "tilt.mustache");
        }, new MustacheTemplateEngine());

        get("/hello", (req, res) -> "Hello World");

        post("/view", (req, res) -> {
            Map<String, Object> data = new HashMap<>();
            try {
                MultiMap<String> params = new MultiMap<>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                String searchName = params.get("requestedSummoner").toString().replace('[', ' ').replace(']', ' ').trim().toLowerCase();
                if ( !searchName.matches("^[0-9\\p{L} _\\.]+$") ){
                    setSearchError(data, searchName);
                    return new ModelAndView(data, "tilt.mustache");
                } else {
                    Summoner summoner = summonerService.findSummoner(searchName, "NA");
                    if ( summoner == null ) {
                        setSearchError(data, searchName);
                        return new ModelAndView(data, "tilt.mustache");
                    }
                    data.put("summonerName", searchName);
                    int check = searchForTwitchGames(summoner);
                    if (check >= 1){
                        data.put("tilted", false);
                        data.put("games", check);
                    } else {
                        data.put("tilted", true);
                        data.put("error", false);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return new ModelAndView(data, "view.mustache");
        }, new MustacheTemplateEngine());

    }

    private static void setSearchError(Map data, String searchName){
        data.put("error", true);
        data.put("summonerName", searchName);
    }

    private static int searchForTwitchGames(Summoner summoner){
        int twitchCount = 0;
        MatchList matches = matchService.getMatchList(summoner.getSummonerId());
        if (matches.getMatchCount() == 0){
            return  twitchCount;
        }
        for (Match match : matches.getMatches()){
            long championId = match.getChampion();
            if (championId == 29){
                twitchCount++;
            }
        }
        return twitchCount;
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    static String getHerokuSecret() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return processBuilder.environment().get("SECRET");
        }
        return null;
    }



}