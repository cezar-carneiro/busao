package com.busao.gyn.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by cezar.carneiro on 17/08/2017.
 */

@Dao
public interface BusStopDao {

    @Update
    public void update(BusStop busStop);

    @Query("SELECT * FROM pontos WHERE pontos.favorito = 1")
    public List<BusStopWithLines> readFavorites();

    @Query("SELECT * FROM pontos WHERE pontos.id = :id")
    public BusStopWithLines readBusStop(Integer id);

    @Query("SELECT * FROM pontos WHERE latitude > :bottom AND latitude < :top AND longitude < :right AND longitude > :left")
    public List<BusStopWithLines> searchByLocation(double top, double right, double bottom, double left);//TODO: in the future we will paginate

    @Query("SELECT * FROM pontos WHERE pontos.codigoPonto LIKE :input OR pontos.endereco LIKE :input OR pontos.pontoReferencia LIKE :input ")
    public List<BusStopWithLines> searchByText(String input);//TODO: in the future we will paginate

}
