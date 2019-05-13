package xyz.ruankun.microweatherbasic.service;
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

import xyz.ruankun.microweatherbasic.vo.WeatherResponse;

/**
 * @since 1.0.0 201905
 * @author <a href="http://www.ruankun.xyz/">murruan<a/>
 */
public interface WeatherDataService {

    /**
     * 根据城市ID查询天气数据
     * @param cityId   城市ID
     * @return  天气数据
     */
    WeatherResponse getDataByCityId(String cityId);

    /**
     * 根据城市ID查询天气数据
     * @param cityName   城市名
     * @return  天气数据
     */
    WeatherResponse getDataByCityName(String cityName);

}
