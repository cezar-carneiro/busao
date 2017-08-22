package com.busao.gyn.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import com.busao.gyn.data.line.BusLine;
import com.busao.gyn.data.stop.BusStop;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

@Entity(tableName = "pontoslinhas",
        primaryKeys = {"codigoPonto", "codigoLinha"},
        foreignKeys = {@ForeignKey(entity = BusStop.class, parentColumns = "codigoPonto", childColumns = "codigoPonto"),
                @ForeignKey(entity = BusLine.class, parentColumns = "codigo", childColumns = "codigoLinha")},
        indices = { @Index(value = {"codigoPonto", "codigoLinha"}, unique = true), @Index(value = "codigoPonto"), @Index(value = "codigoLinha")})
public class LineStop {

    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "codigoPonto")
    private Integer stop;

    @ColumnInfo(name = "codigoLinha")
    private Integer line;

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

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }
}
