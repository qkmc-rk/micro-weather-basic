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
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.ruankun.microweatherbasic.service.WeatherDataService;
import xyz.ruankun.microweatherbasic.vo.WeatherResponse;

import java.io.IOException;
import java.util.Date;

@Service
//@Component
//@Repository
public class WeatherDataServiceImpl implements WeatherDataService {

    //从springboot的配置文件中读取API
    @Value("${micro-weather-basic.api}")
    private String WEATHER_API;

    @Autowired
    private RestTemplate restTemplate;



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


    /**
     * e
     * @param uri
     * @return
     */
    private WeatherResponse doGetWeatherData(String uri){
        String strBody = null;
        WeatherResponse weatherResponse = null;
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(new Date() + "  INFO  ---  " + uri);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri,String.class);
        //via status code to judge false or true
        if(responseEntity.getStatusCodeValue() == 200){
            //request success,to get data
            strBody = responseEntity.getBody();
        }
        System.out.println(new Date() + "  INFO  ---  ");
        //anti serialize the String data to Object
        try {
            weatherResponse = mapper.readValue(strBody, WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherResponse;
    }

}
