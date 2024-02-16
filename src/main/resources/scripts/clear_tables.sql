DELETE
FROM currencies;
DELETE
FROM sqlite_sequence
WHERE name = 'currencies';
DELETE
FROM exchange_rates;
DELETE
FROM sqlite_sequence
WHERE name = 'exchange_rates';