package fr.wildcodeschool.culture;

import android.os.Parcel;
import android.os.Parcelable;

public class Museum implements Parcelable {

    String name;
    int numero;
    String horaires;
    String site;
    String metro;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
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

    public static Creator<Museum> getCREATOR() {
        return CREATOR;
    }


    protected Museum(Parcel in) {
        name = in.readString();
        numero = in.readInt();
        horaires = in.readString();
        site = in.readString();
        metro = in.readString();
    }

    public static final Creator<Museum> CREATOR = new Creator<Museum>() {
        @Override
        public Museum createFromParcel(Parcel in) {
            return new Museum(in);
        }

        @Override
        public Museum[] newArray(int size) {
            return new Museum[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(numero);
        dest.writeString(horaires);
        dest.writeString(site);
        dest.writeString(metro);
    }
}

