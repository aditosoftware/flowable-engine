package org.flowable.aditoDataService;

import com.google.gson.Gson;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
public class RestClientService
{
    private String aditoBaseUrl = "https://host.docker.internal:8443";
    private String aditoUser = "flowableIdmService";
    private String aditoPassword = "HczABCxBEUKSmwQEnT8vbmkE8Bj1hcXOKSbsLWBg";

    private WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(RestClientService.class);

    public WebClient getWebClient ()
    {
        return this.webClient;
    }

    public String getAditoBaseUrl() {
        return aditoBaseUrl;
    }

    public RestClientService (@Value("${aditoUrl:}") String springAditoBaseUrl, @Value("${aditoUser:}") String springAditoUser, @Value("${aditoPassword:}") String springAditoPassword)
    {
        if (environmentVariablesArePresent())
            loadEndpointConfigFromEnvironment();
        else if (springVariablesArePresent(springAditoBaseUrl))
            loadEndpointConfigFromSpring(springAditoBaseUrl, springAditoUser, springAditoPassword);
        else
            logger.info("Using default core connection configuration");

        //TODO: improve ssl
        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            this.webClient = WebClient.builder()
                    .baseUrl(aditoBaseUrl)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .defaultHeaders(headers -> headers.setBasicAuth(aditoUser, aditoPassword))
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean environmentVariablesArePresent()
    {
        String baseUrl = System.getenv("ADITO_URL");
        return (baseUrl != null && !baseUrl.isEmpty());
    }

    private boolean springVariablesArePresent(String pBaseUrl)
    {
        return (pBaseUrl != null && !pBaseUrl.isEmpty());
    }

    private void loadEndpointConfigFromEnvironment()
    {
        logger.info("Loading core connection configuration from environment");
        String baseUrl = System.getenv("ADITO_URL");
        if (baseUrl != null && !baseUrl.isEmpty())
            this.aditoBaseUrl = baseUrl;
        String userName = System.getenv("ADITO_USER");
        if (userName != null && !userName.isEmpty())
            this.aditoUser = userName;
        String password = System.getenv("ADITO_PASSWORD");
        if (password != null && !password.isEmpty())
            this.aditoPassword = password;
    }

    private void loadEndpointConfigFromSpring(String springAditoBaseUrl, String springAditoUser, String springAditoPassword)
    {
        logger.info("Loading core connection configuration from spring application json");
        if (springAditoBaseUrl != null && !springAditoBaseUrl.isEmpty())
            this.aditoBaseUrl = springAditoBaseUrl;
        if (springAditoUser != null && !springAditoUser.isEmpty())
            this.aditoUser = springAditoUser;
        if (springAditoPassword != null && !springAditoPassword.isEmpty())
            this.aditoPassword = springAditoPassword;
    }

    public String get(String url, MultiValueMap<String, String> queryParams, Map<String,String> headers)
    {
        WebClient.RequestHeadersUriSpec<?> spec = getWebClient().get();
        if (queryParams == null || queryParams.isEmpty())
            spec.uri(url);
        else
            spec.uri(uriBuilder -> uriBuilder.path(url).queryParams(queryParams).build());

        if (headers != null)
            headers.forEach(spec::header);
        logger.info("headers: " + new Gson().toJson(headers));
        return spec.retrieve().bodyToMono(String.class).block();
    }

    protected String get(String url)
    {
        return get(url, null, null);
    }

}
