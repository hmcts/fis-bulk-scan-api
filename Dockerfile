ARG APP_INSIGHTS_AGENT_VERSION=3.2.8
FROM hmctspublic.azurecr.io/base/java:17-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/fis-bulk-scan-api.jar /opt/app/

EXPOSE 8090
CMD [ "fis-bulk-scan-api.jar" ]
