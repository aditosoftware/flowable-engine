FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21
ADD wait-for-something.sh .

RUN apk update && apk add fontconfig ghostscript-fonts && rm -rf /var/cache/apk/*

RUN addgroup tomcat && adduser -s /bin/false -G tomcat -h /opt/tomcat -D tomcat

RUN wget http://archive.apache.org/dist/tomcat/tomcat-9/v9.0.14/bin/apache-tomcat-9.0.14.tar.gz -O /tmp/tomcat.tar.gz
RUN cd /tmp && tar xvfz tomcat.tar.gz && cp -Rv /tmp/apache-tomcat-9.0.14/* /opt/tomcat/ && rm -Rf /tmp/apache-tomcat-9.0.14

COPY context.xml /opt/tomcat/conf/context.xml

COPY assets/flowable-idm.war.original /opt/tomcat/webapps/flowable-idm.war
COPY assets/flowable-modeler.war.original /opt/tomcat/webapps/flowable-modeler.war

RUN cd /opt/tomcat && chgrp -R tomcat /opt/tomcat && chmod -R g+r conf && chmod g+x conf && chown -R tomcat webapps/ work/ temp/ logs/ \
    && chown tomcat /wait-for-something.sh && chmod +x /wait-for-something.sh

ENV CATALINA_HOME /opt/tomcat
ENV PATH $PATH:$CATALINA_HOME/bin
ENV JAVA_OPTS="-Xms1024M -Xmx1024M -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

WORKDIR /opt/tomcat

USER tomcat

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
