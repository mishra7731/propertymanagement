-- V2: small seed dataset for local dev and demos.

INSERT INTO property (name, address_line1, city, state, postal_code, property_type, square_feet, year_built, acquired_on, acquisition_price)
VALUES
  ('Gateway Office Park',   '100 Market St',       'Salt Lake City', 'UT', '84101', 'OFFICE',     85000, 2008, '2019-04-15', 24500000.00),
  ('Riverside Logistics',   '4200 Industrial Way', 'Ogden',          'UT', '84401', 'INDUSTRIAL',210000, 2015, '2021-09-01', 38000000.00),
  ('Cottonwood Retail Row', '55 Center Plaza',     'Murray',         'UT', '84107', 'RETAIL',     42000, 2012, '2020-06-30', 16750000.00);

INSERT INTO loan (property_id, lender, principal, interest_rate, term_months, origination_date, maturity_date, status)
VALUES
  (1, 'Wasatch Capital Bank',   17000000.00, 0.0575, 120, '2019-05-01', '2029-05-01', 'ACTIVE'),
  (2, 'Mountain West CRE Fund', 26000000.00, 0.0625,  84, '2021-10-01', '2028-10-01', 'ACTIVE');

INSERT INTO tenant (legal_name, contact_email, contact_phone)
VALUES
  ('Summit Analytics LLC',  'ap@summitanalytics.example', '801-555-0110'),
  ('Beehive Logistics Inc', 'billing@beehivelog.example', '801-555-0142');

INSERT INTO lease (property_id, tenant_id, start_date, end_date, monthly_rent, status)
VALUES
  (1, 1, '2022-01-01', '2027-01-01',  92000.00, 'ACTIVE'),
  (2, 2, '2022-03-01', '2027-03-01', 145000.00, 'ACTIVE');

INSERT INTO payment_record (lease_id, loan_id, amount, paid_on, method)
VALUES
  (1, NULL,  92000.00, '2024-01-02', 'ACH'),
  (1, NULL,  92000.00, '2024-02-01', 'ACH'),
  (NULL, 1, 189500.00, '2024-01-01', 'WIRE');