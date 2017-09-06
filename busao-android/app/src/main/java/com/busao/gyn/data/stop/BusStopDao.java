package com.busao.gyn.data.stop;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by cezar.carneiro on 17/08/2017.
 */

@Dao
public interface BusStopDao {

    @Query("SELECT COUNT(favorito) AS c FROM pontos WHERE favorito = 1")
    int countFavorites();

    @Update
    void update(BusStop busStop);

    @Query("SELECT * FROM pontos WHERE pontos.favorito = 1")
    List<BusStopWithLines> readFavorites();

    @Query("SELECT * FROM pontos WHERE pontos.id = :id")
    BusStopWithLines readBusStop(Integer id);

    @Query("SELECT * FROM pontos WHERE latitude < :topLat AND latitude > :bottomLat AND longitude > :leftLong AND longitude < :rightLong")
    List<BusStopWithLines> searchByLocation(double topLat, double bottomLat, double leftLong, double rightLong);//TODO: in the future we will paginate

    @Query("SELECT * FROM pontos WHERE pontos.codigoPonto LIKE :input OR pontos.endereco LIKE :input OR pontos.pontoReferencia LIKE :input ")
    List<BusStop> searchByText(String input);//TODO: in the future we will paginate

    @Query("SELECT pontos.id , pontos.codigoPonto , pontos.latitude , pontos.longitude , pontos.endereco , pontos.bairro , pontos.cidade , pontos.pontoReferencia , pontos.favorito FROM pontos INNER JOIN pontoslinhas ON pontoslinhas.codigoPonto = pontos.codigoPonto WHERE pontoslinhas.codigoLinha = :line" )
    List<BusStopWithLines> listByLine(Integer line);

}
