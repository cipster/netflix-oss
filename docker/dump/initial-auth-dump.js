/**
 * Creates initial data
 */
print('dump start');

db.users.update(
    {"_id": "admin"},
    {
        "_id": "admin",
        "username": "admin",
        "password": "password",
        "enabled": true,
        "locked": false,
        "authorities": ["USER", "ADMIN"]
    },
    {upsert: true}
);

print('dump complete');