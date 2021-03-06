package tikape.runko.domain;

public class Viesti {

    private Integer id;
    private Integer viestiketju;
    private String teksti;
    private String lahettaja;
    private String lahetysaika;
    private String otsikko;

    public Viesti(Integer id, Integer viestiketju, String teksti, String lahettaja, String lahetysaika, String otsikko) {
        this.id = id;
        this.viestiketju = viestiketju;
        this.teksti = teksti;
        this.lahettaja = lahettaja;
        this.lahetysaika = lahetysaika;
        this.otsikko = otsikko;
    }
    
    public Viesti(Integer id, Integer viestiketju, String teksti, String lahettaja,  String otsikko) {
        this.id = id;
        this.viestiketju = viestiketju;
        this.teksti = teksti;
        this.lahettaja = lahettaja;
        this.otsikko = otsikko;
    }
    
    public String toString() {
        return this.otsikko + ", " + this.lahettaja + ", " + this.teksti;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLahettaja() {
        return lahettaja;
    }
    public Integer getViestiketju() {
        return viestiketju;
    }

    public String getLahetysaika() {
        return lahetysaika;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setLahettaja(String lahettaja) {
        this.lahettaja = lahettaja;
    }

    public void setLahetysaika(String lahetysaika) {
        this.lahetysaika = lahetysaika;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }
    

}
