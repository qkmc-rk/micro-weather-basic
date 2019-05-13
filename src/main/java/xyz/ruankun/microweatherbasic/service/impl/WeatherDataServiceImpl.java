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
     * 该方法根据uri去请求第三方服务器，请求后将获得的数据 反序列化到天气对象中
     * @param uri
     * @return
     */
    private WeatherResponse doGetWeatherData(String uri){
        String strBody = null;
        WeatherResponse weatherResponse = null;
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(new Date() + "  INFO  ---  " + uri);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri,String.class);
        //通过返回的状态码判断请求是否成功
        if(responseEntity.getStatusCodeValue() == 200){
            //请求成功，获取数据
            strBody = responseEntity.getBody();
        }
        System.out.println(new Date() + "  INFO  ---  ");
        //将String数据反序列化到weatherResponse对象当中
        try {
            weatherResponse = mapper.readValue(strBody, WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherResponse;
    }

}
