FROM quay.io/wildfly/wildfly:24.0.0.Final

ARG POSTGRESQL_JDBC_VERSION=42.5.0

ARG WILDFLY_USER
ARG WILDFLY_PASSWORD
ARG PWD

ARG DB_HOST
ARG DB_NAME
ARG DB_USER
ARG DB_PASSWORD


RUN /bin/sh -c '$JBOSS_HOME/bin/standalone.sh &' && \
    sleep 10 && \
    cd /tmp && \
    curl --location --output postgresql-${POSTGRESQL_JDBC_VERSION}.jar --url http://search.maven.org/remotecontent?filepath=org/postgresql/postgresql/${POSTGRESQL_JDBC_VERSION}/postgresql-${POSTGRESQL_JDBC_VERSION}.jar && \
    $JBOSS_HOME/bin/jboss-cli.sh --connect --command="module add --name=org.postgres --dependencies=javax.api,javax.transaction.api --resources=/tmp/postgresql-${POSTGRESQL_JDBC_VERSION}.jar" && \
    $JBOSS_HOME/bin/jboss-cli.sh --connect --command="/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)" && \
    $JBOSS_HOME/bin/jboss-cli.sh --connect --command="data-source add --jndi-name=java:/${DB_NAME}DS --name=${DS_NAME}DS --connection-url=jdbc:postgresql://${DB_HOST}:5432/${DB_NAME} --driver-name=postgres --user-name=${DB_USER} --password=${DB_PASSWORD}" && \
    $JBOSS_HOME/bin/jboss-cli.sh --connect --command=:shutdown && \
    rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/* && \
    rm /tmp/postgresql-${POSTGRESQL_JDBC_VERSION}.jar && \
    rm -rf /tmp/postgresql-${POSTGRESQL_JDBC_VERSION}.jar




RUN /opt/jboss/wildfly/bin/add-user.sh ${WILDFLY_USER} ${WILDFLY_PASSWORD} --silent

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interfaces

ADD admission-system-0.2-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]