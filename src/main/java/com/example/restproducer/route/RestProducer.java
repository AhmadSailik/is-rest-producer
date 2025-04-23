package com.example.restproducer.route;

import com.example.restproducer.model.HelloResponse;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;


@Component
public class RestProducer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("netty-http")
                .contextPath("/api")
                .port(8081)
                .bindingMode(RestBindingMode.json);

        rest("/getName")
                .skipBindingOnErrorCode(true)
                .clientRequestValidation(true)
                .description("testAPI")
                .get().produces("application/json")
                .param().name("name").type(RestParamType.query).dataType("string").required(true).endParam()
                .to("direct:getName");

        from("direct:getName")
                .process(ex->{
                    HelloResponse response = new HelloResponse();
                    response.setName(ex.getIn().getHeader("name",String.class));
                    ex.getIn().setBody(response);
                })
                .log("Return the name successfully");


    }
}
