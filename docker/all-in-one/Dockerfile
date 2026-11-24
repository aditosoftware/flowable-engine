FROM eclipse-temurin:21.0.13.1_2-jre-alpine-3.23
ADD wait-for-something.sh .

RUN apk update && apk add fontconfig ghostscript-fonts && rm -rf /var/cache/apk/*

RUN addgroup tomcat && adduser -s /bin/false -G tomcat -h /opt/tomcat -D tomcat

# Tomcat 9.0.x is the last line supporting javax.servlet (required by Flowable 6.5 / Spring Boot 2.2).
# On version bump, update TOMCAT_SHA512 from the .sha512 file next to the tarball.
ARG TOMCAT_VERSION=9.0.124
ARG TOMCAT_SHA512=925e4fefe25b2ecccd032acd0b4cb1b506455037ed0f8b0e51824bed839c5e92bf5e58982b990d2fdcbc790bcda1a533db7faffb325f8a7baee939fc8ae871c1
RUN wget -q https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/tomcat.tar.gz \
    && echo "${TOMCAT_SHA512}  /tmp/tomcat.tar.gz" | sha512sum -c - \
    && tar xzf /tmp/tomcat.tar.gz -C /opt/tomcat --strip-components=1 \
    && rm -rf /tmp/tomcat.tar.gz /opt/tomcat/webapps/*

COPY context.xml /opt/tomcat/conf/context.xml

COPY assets/flowable-idm.war.original /opt/tomcat/webapps/flowable-idm.war
COPY assets/flowable-modeler.war.original /opt/tomcat/webapps/flowable-modeler.war

RUN cd /opt/tomcat && chgrp -R tomcat /opt/tomcat && chmod -R g+r conf && chmod g+x conf && chown -R tomcat webapps/ work/ temp/ logs/ \
    && chown tomcat /wait-for-something.sh && chmod +x /wait-for-something.sh

ENV CATALINA_HOME=/opt/tomcat
ENV PATH=$PATH:$CATALINA_HOME/bin
ENV JAVA_OPTS="-Xms1024M -Xmx1024M -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

WORKDIR /opt/tomcat

USER tomcat

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
