FROM mongo

COPY mongo-healthcheck.sh ./mongo-healthcheck.sh

HEALTHCHECK CMD ["mongo-healthcheck.sh"]

EXPOSE 27017