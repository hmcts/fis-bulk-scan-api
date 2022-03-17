ARG APP_INSIGHTS_AGENT_VERSION=3.2.4
FROM hmctspublic.azurecr.io/base/java:11-distroless

COPY build/libs/family-bulk-scan-api.jar /opt/app/

EXPOSE 8090
CMD [ "family-bulk-scan-api.jar" ]
