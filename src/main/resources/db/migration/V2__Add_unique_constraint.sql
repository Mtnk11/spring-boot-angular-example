ALTER TABLE customer.public.customer
add CONSTRAINT email_unique_constraint UNIQUE (email);