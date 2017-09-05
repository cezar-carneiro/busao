package com.busao.gyn.data.line;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by cezar.carneiro on 17/08/2017.
 */

@Dao
public interface BusLineDao {

    @Update
    void update(BusLine busLine);

    @Query("SELECT * FROM linhas WHERE linhas.favorita = 1")
    List<BusLinesWithStops> readFavorites();//TODO: in the future we will paginate

    @Query("SELECT * FROM linhas WHERE linhas.codigo LIKE :input OR linhas.nome LIKE :input")
    List<BusLine> searchByText(String input);//TODO: in the future we will paginate

}
