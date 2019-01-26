#!/bin/bash

if test -z "$MONGODB_PASSWORD"; then
    echo "MONGODB_PASSWORD not defined"
    exit 1
fi

AUTH="-u admin -p $MONGODB_PASSWORD"

# MONGODB USER CREATION
(
echo "setup mongodb auth"
create_user="if (!db.getUser('admin')) { db.createUser({ user: 'admin', pwd: '$MONGODB_PASSWORD', roles: [ {role:'readWrite', db:'$DB_NAME'} ]}) }"
until mongo $DB_NAME --eval "$create_user" || mongo $DB_NAME $AUTH --eval "$create_user"; do sleep 5; done
killall mongod
sleep 1
killall -9 mongod
) &

# INIT DUMP EXECUTION
(
if test -n "$INIT_DUMP"; then
    echo "execute dump file"
    pwd
	until mongo $DB_NAME $AUTH $INIT_DUMP; do sleep 5; done
fi
) &

echo "start mongodb without auth"
chown -R mongodb /data/db
gosu mongodb mongod "$@"

echo "restarting with auth on"
sleep 5
exec gosu mongodb /usr/local/bin/docker-entrypoint.sh --auth "$@"
