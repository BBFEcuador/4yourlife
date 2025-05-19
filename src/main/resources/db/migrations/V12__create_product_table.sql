CREATE TABLE IF NOT EXISTS products (
  id TEXT NOT NULL,
  name TEXT NOT NULL,
  code TEXT NOT NULL,
  basePrice FLOAT NOT NULL,
  currency TEXT NOT NULL,
  is_active BOOLEAN NOT NULL,
  description TEXT,
  CONSTRAINT pk_products PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS rules(
  id TEXT NOT NULL,
  description TEXT NOT NULL,
  value FLOAT NOT NULL,
  enabled BOOLEAN NOT NULL,
  product_id TEXT NOT NULL,
  CONSTRAINT fk_products_on_rules FOREIGN KEY (product_id) REFERENCES products (id),
  CONSTRAINT pk_rules PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS programs
(
    id varchar not null,
    name varchar not null,
    courseLevel text not null,
    CONSTRAINT pk_admin_programs PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS programs_products
(
    program_id text not null,
    product_id text not null,
    CONSTRAINT fk_programs_on_programs_products FOREIGN KEY (program_id) REFERENCES programs (id),
    CONSTRAINT fk_products_on_programs_products FOREIGN KEY (product_id) REFERENCES products (id)
);