package com.nxest.grpc.spring.client;

import com.google.common.collect.Lists;
import io.grpc.ClientInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;


public class GlobalClientInterceptorRegistry implements ApplicationContextAware, InitializingBean {

    private final List<ClientInterceptor> clientInterceptors = Lists.newArrayList();
    private ApplicationContext applicationContext;

    public GlobalClientInterceptorRegistry addClientInterceptors(ClientInterceptor interceptor) {
        clientInterceptors.add(interceptor);
        return this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<ClientInterceptor> getClientInterceptors() {
        return clientInterceptors;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, GlobalClientInterceptorConfigurerAdapter> map = applicationContext.getBeansOfType(GlobalClientInterceptorConfigurerAdapter.class);
        for (GlobalClientInterceptorConfigurerAdapter globalClientInterceptorConfigurerAdapter : map.values()) {
            globalClientInterceptorConfigurerAdapter.addClientInterceptors(this);
        }
    }
}
