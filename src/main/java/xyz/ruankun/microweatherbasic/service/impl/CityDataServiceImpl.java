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

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import xyz.ruankun.microweatherbasic.service.CityDataService;
import xyz.ruankun.microweatherbasic.util.XmlBuilder;
import xyz.ruankun.microweatherbasic.vo.jaxb.Country;

import java.io.*;

@Service
public class CityDataServiceImpl implements CityDataService {

    @Override
    public Country getCountry() throws Exception {

        /**
         * 1.read resource
         * 2.as inputStream
         * 3.as buffer
         * 4.as string buffer
         * 5.use xml builder
         */
        Resource resource = new ClassPathResource("citylist.xml");
        InputStream inputStream = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        //StringBuffer is synchronized
        StringBuffer stringBuffer = new StringBuffer();
        //variable line's initialization is under
        String line;

        //write data to string buffer read from
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        bufferedReader.close();

        Country country = (Country) XmlBuilder.xmlStrToObject(Country.class,stringBuffer.toString());
        return country;
    }
}
