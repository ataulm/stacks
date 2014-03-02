CREATE TABLE IF NOT EXISTS 'stacks' (
        _id TEXT PRIMARY KEY NOT NULL,
        parent TEXT NOT NULL,
        summary TEXT NOT NULL,
        description TEXT NOT NULL,
        leaf_count INTEGER NOT NULL,
        position INTEGER NOT NULL,
        created INTEGER NOT NULL,
        modified INTEGER NOT NULL,
        deleted INTEGER NOT NULL
);

INSERT INTO stacks(_id, parent, summary, description, leaf_count, position, created, modified, deleted) VALUES (
        'id_zero',
        'as_batman',
        'zero',
        'this is root stack.',
        0,
        0,
        -2208988800000,
        -2208988800000,
        -2208988800000);
