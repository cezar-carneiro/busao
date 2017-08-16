package com.busao.gyn.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

@Entity(tableName = "pontoslinhas")
public class LineStop {

    @PrimaryKey
    private Integer id;

    @ColumnInfo(name = "codigoPonto")
    private Integer stop;

    @ColumnInfo(name = "codigoLinha")
    private Integer linha;

    public LineStop() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStop() {
        return stop;
    }

    public void setStop(Integer stop) {
        this.stop = stop;
    }

    public Integer getLinha() {
        return linha;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }
}
