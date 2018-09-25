FROM or-dev-base:latest

USER root
ENV JBOSS_HOME=/deployment/jboss-4.0.2
WORKDIR ${JBOSS_HOME}
ADD run-online-review.sh /deployment/bin

EXPOSE 8080

ENTRYPOINT [ "bash", "-c" ]
CMD [ "/deployment/bin/run-online-review.sh" ]