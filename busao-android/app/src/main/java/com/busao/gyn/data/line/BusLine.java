package com.busao.gyn.data.line;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

@Entity(tableName = "linhas")
public class BusLine {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "codigo")
    private Integer code;

    @ColumnInfo(name = "nome")
    private String description;

    @ColumnInfo(name = "favorita")
    private Integer favorite;

    public BusLine() {

    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

