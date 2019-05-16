package xyz.ruankun.microweatherbasic.job;
//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                           O\  =  /O
//                        ____/`---'\____
//                      .'  \\|     |//  `.
//                     /  \\|||  :  |||//  \
//                    /  _||||| -:- |||||-  \
//                    |   | \\\  -  /// |   |
//                    | \_|  ''\---/''  |   |
//                    \  .-\__  `-`  ___/-. /
//                  ___`. .'  /--.--\  `. . __
//               ."" '<  `.___\_<|>_/___.'  >'"".
//              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//              \  \ `-.   \_ __\ /__ _/   .-` /  /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//                      Buddha Bless, No Bug !

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import xyz.ruankun.microweatherbasic.service.CityDataService;
import xyz.ruankun.microweatherbasic.service.WeatherDataService;
import xyz.ruankun.microweatherbasic.vo.jaxb.City;
import xyz.ruankun.microweatherbasic.vo.jaxb.Country;
import xyz.ruankun.microweatherbasic.vo.jaxb.County;
import xyz.ruankun.microweatherbasic.vo.jaxb.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * use quartz to realise the weather data sync from third api
 *
 * this class is to sync weather data from third api
 */
public class WeatherDataSyncJob extends QuartzJobBean {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataSyncJob.class);

    @Autowired
    private CityDataService cityDataService;

    @Autowired
    private WeatherDataService weatherDataService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //log
        logger.info("weather data sync job starts!");
        logger.info("start to get city data from classpath:citylist.xml");
        Country country = null;
        try {
            country = cityDataService.getCountry();
        } catch (Exception e) {
            logger.error("get city data from classpath:citylist.xml failed");
            e.printStackTrace();
        }
        //logger.info("--------------->" + country.toString());
        List<Province> provinceList = country.getProvinceList();
        //for each the list add get cityList;
        List<City> cityList = new ArrayList<>();
        for (Province p :
                provinceList) {
            cityList.addAll(p.getCityList());
        }
        //for each and get county list
        List<County> countyList = new ArrayList<>();
        for (City c :
                cityList) {
            countyList.addAll(c.getCountyList());
        }

        //start to sync data
        logger.info("start to sync...");
        for (County c :
                countyList) {
            logger.info("syncing,cityid is:" + c.getId() + ",name:" + c.getName() + " ,weatherCode:" + c.getWeatherCode());
            //sync data by weather code.
            weatherDataService.syncDataByCityId(c.getWeatherCode());
        }
        logger.info("weather data sync finished!");
    }
}
