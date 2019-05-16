package xyz.ruankun.microweatherbasic.util;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;

/**
 * this util is to convert xml String to Object.
 */
public class XmlBuilder {

    /**
     *a util for converting xml str to object
     *
     * @param clazz the type this object should be.
     * @param xmlStr String of xml data.
     * @return object with xml data
     * @throws Exception
     */
    public static Object xmlStrToObject(Class<?> clazz, String xmlStr) throws Exception{
        Object xmlObject = null;
        Reader reader = null;

        JAXBContext context = JAXBContext.newInstance(clazz);
        //xmlStr parsing core method
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //1.read str.   2.use unmarshaller to parse to object
        reader = new StringReader(xmlStr);
        xmlObject = unmarshaller.unmarshal(reader);

        if(null != reader)
            reader.close();
        return xmlObject;
    }
}
