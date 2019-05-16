package xyz.ruankun.microweatherbasic.service.impl;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.ruankun.microweatherbasic.service.WeatherDataService;
import xyz.ruankun.microweatherbasic.vo.WeatherResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
//@Component
//@Repository
public class WeatherDataServiceImpl implements WeatherDataService {

    //to write log
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataServiceImpl.class);
    //从springboot的配置文件中读取API
    @Value("${micro-weather-basic.api}")
    private String WEATHER_API;
    @Autowired
    private RestTemplate restTemplate;
    // use to operate the data in/out redis
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //how long the data will out of date from cache(redis)
    @Value("${micro-weather-basic.redis.timeout}")
    private Long TIME_OUT;

    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        String uri = WEATHER_API + "?citykey=" + cityId;
        return this.doGetWeatherData(uri);
    }

    @Override
    public WeatherResponse getDataByCityName(String cityName) {
        String uri = WEATHER_API + "?city=" + cityName;
        return this.doGetWeatherData(uri);
    }

    @Override
    public void syncDataByCityId(String cityId) {
        String uri = WEATHER_API + "?citykey=" + cityId;
        this.saveWeatherData(uri);
    }

    /**
     * e
     * @param uri
     * @return
     */
    private WeatherResponse doGetWeatherData(String uri){
        //get the operator provided by stringRedisTemplate to operate the K-V data
        ValueOperations ops = this.stringRedisTemplate.opsForValue();
        //uri as the key in redis
        String key = uri;
        String strBody = null;
        //search data from redis first.
        if(!this.stringRedisTemplate.hasKey(uri)){
            //sorry but there is no cache
            logger.info("sorry there is no weather info cache in redis");
            logger.info("start requesting server at:" + uri);
            strBody = getBody(restTemplate,uri);
            //since we have gotten the data.We cache it.
            ops.set(uri,strBody,TIME_OUT, TimeUnit.SECONDS);
        }else{
            //find cache in redis
            logger.info("find cache in redis server" );
            logger.info("will not request the api for weather data,request uri:" + uri);
            strBody = (String) ops.get(uri);
        }
        //anti serialize the String data to Object
        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse weatherResponse = null;
        try {
            weatherResponse = mapper.readValue(strBody, WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherResponse;
    }

    /**
     * get the data from the uri and then save these data to cache (redis)
     * @param uri
     */
    private void saveWeatherData(String uri){
        ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();
        String key = uri;
        String strBody = null;
        if(valueOperations.get(uri) != null){
            logger.info("data exits in cache(redis),key is:" + uri);
        }else{
            strBody = getBody(restTemplate,uri);
            valueOperations.set(key, strBody, TIME_OUT, TimeUnit.SECONDS);
        }
    }

    /**
     * this method is to reduce repeat code used by two methods up.
     * @param restTemplate
     * @param uri
     * @return
     */
    private String getBody(RestTemplate restTemplate,String uri){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        if(responseEntity.getStatusCodeValue() == 200)
            return responseEntity.getBody();
        return null;
    }
}
