package cn.fan.penguin.debug.spring;

import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.SingerTotalRequest;
import cn.fan.penguin.debug.request.SingerInfoRequestImpl;
import cn.fan.penguin.debug.request.SingerListRequestImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import javax.xml.ws.WebEndpoint;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/26
 * @Created by fanduanjin
 */

@Configuration
public class PenguinDebugAutoConfig {
    @Autowired
    ObjectMapper objectMapper;

    @Bean
    PenguinRequestParameterCreator penguinRequestParameterCreator() {
        return new PenguinRequestParameterCreator(objectMapper);
    }

    @Bean
    SingerTotalRequest singerTotalRequest() {
        return new SingerTotalRequest(penguinRequestParameterCreator());
    }

    @Bean
    SingerListRequestImpl singerListRequest() {
        return new SingerListRequestImpl(penguinRequestParameterCreator());
    }

    @Bean
    SingerInfoRequestImpl singerInfoRequest() {
        return new SingerInfoRequestImpl(penguinRequestParameterCreator());
    }

}
