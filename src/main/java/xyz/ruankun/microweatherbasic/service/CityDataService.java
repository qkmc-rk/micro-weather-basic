package xyz.ruankun.microweatherbasic.service;

import xyz.ruankun.microweatherbasic.vo.jaxb.Country;

/**
 * this interface is to define how to get city data from xml file!!
 */
public interface CityDataService {

    /**
     * all city data exists in country element.
     * @return country info
     */
    Country getCountry() throws Exception;
}
