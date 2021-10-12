package org.flowable.aditoDataService;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Service
public class BaseAditoDataService
{
    @Value("${aditoUrl:https://host.docker.internal:8443}")
    protected String aditoBaseUrl;

    @Value("${aditoUser:flowableIdmService}")
    protected String aditoUser;

    @Value("${aditoPassword:HczABCxBEUKSmwQEnT8vbmkE8Bj1hcXOKSbsLWBg}")
    protected String aditoPassword;

    protected WebClient.Builder getWebClientBuilder () throws SSLException
    {
        SslContext sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
            .baseUrl(aditoBaseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeaders(headers -> headers.setBasicAuth(aditoUser, aditoPassword))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    protected boolean canCallWebservices ()
    {
        return aditoBaseUrl != null && !aditoBaseUrl.isEmpty();
    }
}
