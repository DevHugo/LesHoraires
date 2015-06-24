package com.amine.horaires.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable {
    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };
    private int id;
    private String name;
    private String adresse;
    private String zip;
    private String lat;
    private String lng;
    private String tel;
    private String city;
    private boolean ouvert;
    private String horaires;
    private String url;
    private String openStatus;
    private boolean parking;
    private boolean wifi;
    private boolean AccesHandicape;

    public Shop() {
    }

    public Shop(int id, String name, String adresse, String zip, String lat,
                String lng, String tel, String city,
                boolean ouvert, String horaires, String url, String openStatus,
                boolean parking, boolean wifi, boolean accesHandicape) {
        this.id = id;
        this.name = name;
        this.adresse = adresse;
        this.zip = zip;
        this.lat = lat;
        this.lng = lng;
        this.tel = tel;
        this.city = city;
        this.ouvert = ouvert;
        this.horaires = horaires;
        this.url = url;
        this.openStatus = openStatus;
        this.parking = parking;
        this.wifi = wifi;
        AccesHandicape = accesHandicape;
    }

    private Shop(Parcel in) {
        id = in.readInt();
        name = in.readString();
        adresse = in.readString();
        zip = in.readString();
        lat = in.readString();
        lng = in.readString();
        tel = in.readString();
        city = in.readString();
        ouvert = in.readByte() != 0;
        horaires = in.readString();
        url = in.readString();
        openStatus = in.readString();
        parking = in.readByte() != 0;
        wifi = in.readByte() != 0;
        AccesHandicape = in.readByte() != 0;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adresse='" + adresse + '\'' +
                ", zip='" + zip + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", tel='" + tel + '\'' +
                ", city='" + city + '\'' +
                ", ouvert=" + ouvert +
                ", horaires='" + horaires + '\'' +
                ", url='" + url + '\'' +
                ", openStatus='" + openStatus + '\'' +
                ", parking=" + parking +
                ", wifi=" + wifi +
                ", AccesHandicape=" + AccesHandicape +
                '}';
    }

    public boolean isAccesHandicape() {
        return AccesHandicape;
    }

    public void setAccesHandicape(boolean accesHandicape) {
        AccesHandicape = accesHandicape;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public boolean isOuvert() {
        return ouvert;
    }

    public void setOuvert(boolean ouvert) {
        this.ouvert = ouvert;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(adresse);
        dest.writeString(zip);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(tel);
        dest.writeString(city);
        dest.writeByte((byte) (ouvert ? 1 : 0));
        dest.writeString(horaires);
        dest.writeString(url);
        dest.writeString(openStatus);
        dest.writeByte((byte) (parking ? 1 : 0));
        dest.writeByte((byte) (wifi ? 1 : 0));
        dest.writeByte((byte) (AccesHandicape ? 1 : 0));
    }
}