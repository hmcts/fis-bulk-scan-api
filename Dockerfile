ARG APP_INSIGHTS_AGENT_VERSION=3.7.1
FROM hmctsprod.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/fis-bulk-scan-api.jar /opt/app/

EXPOSE 8090
CMD [ "fis-bulk-scan-api.jar" ]
