CREATE TABLE IF NOT EXISTS currencies (
id INTEGER PRIMARY KEY AUTOINCREMENT,
code VARCHAR(5) NOT NULL UNIQUE,
full_name VARCHAR(65) NOT NULL,
sign VARCHAR(5) NOT NULL);
CREATE TABLE IF NOT EXISTS exchange_rates (
id INTEGER PRIMARY KEY AUTOINCREMENT,
base_currency_id INTEGER NOT NULL,
target_currency_id INTEGER NOT NULL,
rate DECIMAL(10, 6) NOT NULL,
UNIQUE (base_currency_id, target_currency_id),
FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
FOREIGN KEY (target_currency_id) REFERENCES currencies(id));