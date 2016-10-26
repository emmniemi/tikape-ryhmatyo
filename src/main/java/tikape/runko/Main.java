package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
        database.init();
        
        Spark.staticFileLocation("public");
        
        ViestiDao viestiDao = new ViestiDao(database);
        AihealueDao aihealueDao = new AihealueDao(database);
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        
      
        get("/", (req, res) -> {
            res.redirect("/aihealueet");
            return "ok";
        });
        
        get("/aihealueet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aihealueet", aihealueDao.findAll());
            map.put("viestiketjujenLukumaarat", aihealueDao.haeViestiketjujenMaara());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/viestihaku", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("haetutViestit", viestiDao.haeHakusanalla(req.queryParams("hakusana")));
            map.put("haetutAihealueet", aihealueDao.haeAiheenNimi(aihealueDao.haeAihealueHakusanalla(req.queryParams("hakusana"))));         
            
            
           return new ModelAndView(map, "viestihaku"); 
        }, new ThymeleafTemplateEngine());
        

        
        get("/aihealueet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            
            map.put("aihealuetunnus", req.params(":id"));
            map.put("viestienLukumaarat", viestiketjuDao.haeViestienMaara(Integer.parseInt(req.params(":id"))));
            map.put("aihealueennimi", aihealueDao.haeAiheenNimi(Integer.parseInt(req.params(":id"))));
            map.put("viestiketjut", aihealueDao.haeViestiketjut(Integer.parseInt(req.params("id"))));
            

            return new ModelAndView(map, "viestiketjut");
        }, new ThymeleafTemplateEngine());
        
        get("/aihealueet/:aid/:id", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("aihealueennimi", aihealueDao.haeAiheenNimi(Integer.parseInt(req.params(":aid"))));
            map.put("viestiketjunnimi", viestiketjuDao.haeViestiketjunNimi(Integer.parseInt(req.params(":id"))));
            map.put("viestit", viestiketjuDao.haeViestit(Integer.parseInt(req.params("id"))));
            map.put("aihealuetunnus", req.params(":aid"));
            map.put("viestiketjutunnus", req.params(":id"));
            
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
        
        post("/aihealueet/:aid/:id", (req, res) -> {
            viestiDao.lisaaViesti(Integer.parseInt(req.params(":id")), req.queryParams("otsikko"), req.queryParams("lahettaja"),
                    req.queryParams("teksti"));
            res.redirect("/aihealueet/" + req.params(":aid") + "/" + req.params(":id"));
            return "ok";
        });
        
        post("/aihealueet", (req, res) -> {
            aihealueDao.lisaaAihealue(req.queryParams("aiheenNimi"));
            res.redirect("/aihealueet");
            return "ok";
        });
        
        post("/aihealueet/:aid", (req, res) -> {
            viestiketjuDao.lisaaViestiketju(Integer.parseInt(req.params(":aid")), req.queryParams("nimi"));
            res.redirect("/aihealueet/" + req.params(":aid"));
            return "ok";
        });
        
        
        

    }
}
