package com.busao.gyn.data.stop;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by cezar on 23/01/17.
 */
@Entity(tableName = "pontos",
        indices = {@Index(value = "codigoPonto", unique = true), @Index(value = {"latitude", "longitude", "endereco", "bairro", "cidade", "pontoReferencia"})})
public class BusStop implements Serializable{

    @PrimaryKey
    private Integer id;

    @ColumnInfo(name = "codigoPonto")
    private Integer code;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "endereco")
    private String address;

    @ColumnInfo(name = "bairro")
    private String neighborhood;

    @ColumnInfo(name = "cidade")
    private String city;

    @ColumnInfo(name = "pontoReferencia")
    private String reference;

    @ColumnInfo(name = "favorito")
    private Integer favorite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    @Ignore
    public void setFavorite(boolean isFavorite) {
        this.favorite = (isFavorite ? 1 : 0);
    }

    @Ignore
    public boolean isFavorite(){
        return (favorite != null && favorite == 1);
    }

}
