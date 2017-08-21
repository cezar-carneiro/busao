package com.busao.gyn.data.stop;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.busao.gyn.data.LineStop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

public class BusStopWithLines implements Serializable {

    @Embedded
    private BusStop stop;

    @Relation(
            parentColumn = "codigoPonto",
            entityColumn = "codigoPonto",
            entity = LineStop.class)
    private List<LineStop> lines;

    @Ignore
    private String formatedLinesString;

    public BusStopWithLines() {

    }

    public BusStop getStop() {
        return stop;
    }

    public void setStop(BusStop stop) {
        this.stop = stop;
    }

    public List<LineStop> getLines() {
        return lines;
    }

    public void setLines(List<LineStop> lines) {
        this.lines = lines;
    }

    @Ignore
    public String getFormatedLines(){
        if(formatedLinesString != null) {
            return formatedLinesString;
        }

        formatedLinesString = formatLines();
        return formatedLinesString;
    }

    @Ignore
    private String formatLines(){
        /* maybe creating this method was excessive, but, well, crucify me */
        if(lines == null || lines.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(lines.size() * 3);
        int minLen = 3;

        if(lines.size() == 1){
            formatSingleLine(sb, minLen, lines.get(0).getLine());
            return sb.toString();
        }

        for (LineStop ls: lines) {
            formatSingleLine(sb, minLen, ls.getLine());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);

        return sb.toString();
    }

    @Ignore
    private void formatSingleLine(StringBuilder sb, int minLen, Integer line){
        String str = String.valueOf(line);
        for(int i = 0; i + str.length() < minLen; i++){
            sb.append("0");
        }
        sb.append(str);
    }
}
