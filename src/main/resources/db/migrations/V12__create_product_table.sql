CREATE TABLE IF NOT EXISTS products (
  id TEXT NOT NULL,
  name TEXT NOT NULL,
  code TEXT NOT NULL,
  basePrice TEXT NOT NULL,
  currency TEXT NOT NULL,
  is_active BOOLEAN NOT NULL,
  description TEXT,
  CONSTRAINT pk_products PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS rules (
  id TEXT NOT NULL,
  description TEXT NOT NULL,
  value FLOAT NOT NULL,
  product_id TEXT NOT NULL,
  CONSTRAINT fk_products_on_rules FOREIGN KEY (product_id) REFERENCES products (id),
  CONSTRAINT pk_rules PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS programs
(
    id varchar not null,
    name varchar not null,
    level integer not null,
    product_id TEXT not null,
    CONSTRAINT fk_products_on_programs FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT pk_admin_programs PRIMARY KEY (id)
);