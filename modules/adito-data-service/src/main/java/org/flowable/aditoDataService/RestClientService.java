package org.flowable.aditoDataService;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@Service
public class RestClientService
{
    private String aditoBaseUrl = "https://host.docker.internal:8443";
    private String aditoUser = "flowableIdmService";
    private String aditoPassword = "HczABCxBEUKSmwQEnT8vbmkE8Bj1hcXOKSbsLWBg";

    private WebClient webClient;

    public WebClient getWebClient ()
    {
        return this.webClient;
    }

    public String getAditoBaseUrl() {
        return aditoBaseUrl;
    }

    public RestClientService ()
    {
        readEndpointConfigFromEnvironment();
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

    private void readEndpointConfigFromEnvironment ()
    {
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

    public String get(String url, MultiValueMap<String, String> queryParams, Map<String,String> headers)
    {

        if (queryParams == null || queryParams.isEmpty())
            return get(url);
        WebClient.RequestHeadersSpec<?> spec = getWebClient().get().uri(uriBuilder -> uriBuilder.path(url).queryParams(queryParams).build());
        if (headers != null)
            headers.forEach(spec::header);
        return spec.retrieve().bodyToMono(String.class).block();
    }

    protected String get(String url)
    {
        WebClient.RequestHeadersSpec<?> spec = getWebClient().get().uri(url);
        return spec.retrieve().bodyToMono(String.class).block();
    }

}
