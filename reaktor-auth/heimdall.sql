/*
Example of targets:

Mehmaan
    Host
        EventGroup
            Event
                User <- role
    role <- permission

FastOrder
    Owner
        Restaurant
            User <- role
    role <- permission

Resources:
    https://chatgpt.com/c/66e56079-33cc-8012-95e0-05f44d0a2b3e
    https://supabase.com/docs/guides/database/postgres/custom-claims-and-role-based-access-control-rbac
    https://www.youtube.com/watch?v=b6VhN_HHDiQ&t=2827s

Multi Tenant role Based Access Control

Requirements:
An app can have multiple entities.
Mehmaan has many events and FoodKafka has many restaurants.
The same user can have different roles in different entities in the same app.
The role definitions cannot change inside of an app, but a user's roles can change.

roles and permissions are scoped to an app and are unique within it.
roles and users are scoped to an entity and are unique within it.
*/

DROP SCHEMA IF EXISTS heimdall CASCADE;
CREATE SCHEMA IF NOT EXISTS heimdall;
SET search_path TO heimdall;

CREATE TABLE IF NOT EXISTS app(
    id BIGSERIAL PRIMARY KEY,
    name varchar(100),
    data jsonb,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS entity(
    id BIGSERIAL PRIMARY KEY,
    name varchar(100),
    data jsonb,
    app_id BIGSERIAL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (name, app_id),
    FOREIGN KEY (app_id) REFERENCES app(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS "user"(
    id BIGSERIAL PRIMARY KEY,
    name varchar(100),
    social_id text,
    provider text,
    app_id BIGSERIAL,
    data jsonb,
    UNIQUE (social_id, app_id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (app_id) REFERENCES app(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS role(
    id BIGSERIAL PRIMARY KEY,
    name varchar(50),
    app_id BIGSERIAL,
    UNIQUE (name, app_id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (app_id) REFERENCES app(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS permission(
    id BIGSERIAL PRIMARY KEY,
    name varchar(100),
    app_id BIGSERIAL,
    UNIQUE (name, app_id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (app_id) REFERENCES app(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS role_permissions(
    id BIGSERIAL PRIMARY KEY,
    role_id BIGSERIAL,
    permission_id BIGSERIAL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_role(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL,
    role_id BIGSERIAL,
    entity_id BIGSERIAL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (entity_id) REFERENCES entity(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS session(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES "user"(id),
    app_id BIGINT REFERENCES app(id),
    refresh_token TEXT NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION on_update()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at_apps BEFORE UPDATE ON app FOR EACH ROW EXECUTE FUNCTION on_update();
CREATE TRIGGER set_updated_at_entities BEFORE UPDATE ON entity FOR EACH ROW EXECUTE FUNCTION on_update();
CREATE TRIGGER set_updated_at_users BEFORE UPDATE ON "user" FOR EACH ROW EXECUTE FUNCTION on_update();
CREATE TRIGGER set_updated_at_roles BEFORE UPDATE ON role FOR EACH ROW EXECUTE FUNCTION on_update();
CREATE TRIGGER set_updated_at_permissions BEFORE UPDATE ON permission FOR EACH ROW EXECUTE FUNCTION on_update();
CREATE TRIGGER set_updated_at_role_permissions BEFORE UPDATE ON role_permissions FOR EACH ROW EXECUTE FUNCTION on_update();
CREATE TRIGGER set_updated_at_user_roles BEFORE UPDATE ON user_role FOR EACH ROW EXECUTE FUNCTION on_update();

CREATE INDEX idx_entity_app_id ON entity(app_id);
CREATE INDEX idx_user_app_id ON "user"(app_id);
CREATE INDEX idx_role_app_id ON role(app_id);
CREATE INDEX idx_permission_app_id ON permission(app_id);
CREATE INDEX idx_role_permissions_role_id ON role_permissions(role_id);
CREATE INDEX idx_role_permissions_permission_id ON role_permissions(permission_id);
CREATE INDEX idx_user_role_user_id ON user_role(user_id);
CREATE INDEX idx_user_role_role_id ON user_role(role_id);
CREATE INDEX idx_user_role_entity_id ON user_role(entity_id);

INSERT INTO app values
    (0, 'root', '{}', NOW(), NOW()),
    (1, 'BestBuds', '{}', NOW(), NOW()),
    (2, 'Mehmaan', '{}', NOW(), NOW()),
    (3, 'Manna', '{}', NOW(), NOW());

INSERT INTO "user" values
    (0, 'root', 'social_id_here', 'root', 0, '{}', NOW(), NOW())
;

INSERT INTO role values
    (0, 'root', 0, NOW(), NOW())
;