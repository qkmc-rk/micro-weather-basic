package xyz.ruankun.microweatherbasic.service;

import xyz.ruankun.microweatherbasic.vo.Weather;

public interface WeatherReportService {

    /**
     * get weather data by id
     * @param cityId
     * @return
     */
    Weather getDataByCityId(String cityId);
}
