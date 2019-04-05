package fr.wildcodeschool.culture;

public class Museum {
    String name;
    String numero;
    String horaires;
    String site;
    String metro;

    public Museum(String name, String numero, String horaires, String site, String metro) {
        this.name = name;
        this.numero = numero;
        this.horaires = horaires;
        this.site = site;
        this.metro = metro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }
}

