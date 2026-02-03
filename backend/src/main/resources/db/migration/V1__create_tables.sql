CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE artist (
  id UUID PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_artist_name ON artist(name);

CREATE TABLE album (
  id UUID PRIMARY KEY,
  artist_id UUID NOT NULL REFERENCES artist(id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  cover_object_key VARCHAR(400),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_album_artist_id ON album(artist_id);
CREATE INDEX idx_album_title ON album(title);

CREATE TABLE refresh_token (
  id UUID PRIMARY KEY,
  user_id UUID,
  token VARCHAR(500) NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  revoked BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_token_user_id ON refresh_token(user_id);

CREATE TABLE regional (
  id INTEGER PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,
  ativo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_regional_ativo ON regional(ativo);
