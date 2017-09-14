package com.busao.gyn.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */

public interface IForecastDataSource {

    void fetchSchedule(@NonNull Integer stop);
    void fetchSchedule(@NonNull Integer stop, @Nullable Integer line);

}
