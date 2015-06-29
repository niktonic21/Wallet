package sk.upjs.ics.wallet;

/**
 * Created by Maťo21 on 28. 6. 2015.
 */
public class Transakcie {

    private String idUzivatel;
    private String typ;
    private String suma;
    private String polozka;
    private String day;
    private String month;
    private String year;

    public Transakcie(String idUzivatel, String typ, String suma, String polozka, String day, String month, String year) {
        this.idUzivatel = idUzivatel;
        this.typ = typ;
        this.suma = suma;
        this.polozka = polozka;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Transakcie() {
    }

    @Override
    public String toString() {
        return "Transakcie{" +
                "idUzivatel='" + idUzivatel + '\'' +
                ", typ='" + typ + '\'' +
                ", suma='" + suma + '\'' +
                ", polozka='" + polozka + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    public String getIdUzivatel() {
        return idUzivatel;
    }

    public void setIdUzivatel(String idUzivatel) {
        this.idUzivatel = idUzivatel;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }

    public String getPolozka() {
        return polozka;
    }

    public void setPolozka(String polozka) {
        this.polozka = polozka;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}