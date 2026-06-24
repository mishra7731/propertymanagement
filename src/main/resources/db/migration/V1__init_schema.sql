-- V1: core schema for the CRE portfolio domain.
-- Money is NUMERIC(14,2); every FK and searched column is indexed;
-- updated_at is maintained by a trigger.

CREATE TABLE property (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(200)  NOT NULL,
    address_line1 VARCHAR(200)  NOT NULL,
    city          VARCHAR(120)  NOT NULL,
    state         VARCHAR(2)    NOT NULL,
    postal_code   VARCHAR(10)   NOT NULL,
    property_type VARCHAR(30)   NOT NULL,
    square_feet   INTEGER       NOT NULL CHECK (square_feet > 0),
    year_built    SMALLINT,
    acquired_on   DATE,
    acquisition_price NUMERIC(14,2) CHECK (acquisition_price >= 0),
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_property_city_state ON property (city, state);
CREATE INDEX idx_property_type       ON property (property_type);
CREATE INDEX idx_property_name       ON property (lower(name));

CREATE TABLE loan (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    property_id     BIGINT        NOT NULL REFERENCES property (id) ON DELETE CASCADE,
    lender          VARCHAR(200)  NOT NULL,
    principal       NUMERIC(14,2) NOT NULL CHECK (principal > 0),
    interest_rate   NUMERIC(5,4)  NOT NULL CHECK (interest_rate >= 0),
    term_months     INTEGER       NOT NULL CHECK (term_months > 0),
    origination_date DATE         NOT NULL,
    maturity_date   DATE          NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT chk_loan_dates CHECK (maturity_date > origination_date)
);

CREATE INDEX idx_loan_property ON loan (property_id);
CREATE INDEX idx_loan_status   ON loan (status);

CREATE TABLE tenant (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    legal_name    VARCHAR(200) NOT NULL,
    contact_email VARCHAR(254),
    contact_phone VARCHAR(30),
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_tenant_legal_name ON tenant (lower(legal_name));

CREATE TABLE lease (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    property_id   BIGINT        NOT NULL REFERENCES property (id) ON DELETE CASCADE,
    tenant_id     BIGINT        NOT NULL REFERENCES tenant (id)   ON DELETE RESTRICT,
    start_date    DATE          NOT NULL,
    end_date      DATE          NOT NULL,
    monthly_rent  NUMERIC(14,2) NOT NULL CHECK (monthly_rent >= 0),
    status        VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT chk_lease_dates CHECK (end_date > start_date)
);

CREATE INDEX idx_lease_property ON lease (property_id);
CREATE INDEX idx_lease_tenant   ON lease (tenant_id);
CREATE INDEX idx_lease_status   ON lease (status);

CREATE TABLE payment_record (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lease_id      BIGINT        REFERENCES lease (id) ON DELETE CASCADE,
    loan_id       BIGINT        REFERENCES loan (id)  ON DELETE CASCADE,
    amount        NUMERIC(14,2) NOT NULL CHECK (amount > 0),
    paid_on       DATE          NOT NULL,
    method        VARCHAR(20)   NOT NULL DEFAULT 'ACH',
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT chk_payment_target CHECK (
        (lease_id IS NOT NULL AND loan_id IS NULL) OR
        (lease_id IS NULL AND loan_id IS NOT NULL)
    )
);

CREATE INDEX idx_payment_lease   ON payment_record (lease_id);
CREATE INDEX idx_payment_loan    ON payment_record (loan_id);
CREATE INDEX idx_payment_paid_on ON payment_record (paid_on);

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_property_updated BEFORE UPDATE ON property
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER trg_loan_updated     BEFORE UPDATE ON loan
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER trg_tenant_updated   BEFORE UPDATE ON tenant
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER trg_lease_updated    BEFORE UPDATE ON lease
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
