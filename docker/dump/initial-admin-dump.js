/**
 * Creates initial data
 */
print('dump start');

db.tenants.update(
    {"_id": "admin"},
    {
        "_id": "admin",
        "tenant": "admin",
        "enabled": true,
        "locked": false
    },
    {upsert: true}
);

print('dump complete');