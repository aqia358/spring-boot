package com.swiftsnail.controler;

import com.google.gson.Gson;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yaoxm on 2016/3/9.
 */
@RestController
@Slf4j
@RequestMapping("/log")
@CommonsLog
public class DspController {

    protected AtLeastOnceKafkaProducer kafkaProducer = AtLeastOnceKafkaProducer.getInstance();

    String kafkaTopic = "facebook";

    @RequestMapping("/collector")
    String decodeImpr(HttpServletRequest request) {
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }

        Gson gson = new Gson();
        String result = gson.toJson(map);
        log.info(result);
        System.out.println("------------------------------");
        System.out.println(result);
        System.out.println("------------------------------");
        kafkaProducer.send(kafkaTopic, result);
        return "success";
    }

    @RequestMapping("/ping")
    String ping(){
        return "pong";
    }

}
