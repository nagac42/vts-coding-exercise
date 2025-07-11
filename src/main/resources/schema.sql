-- Create user table
CREATE TABLE "user" (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name nvarchar(100) UNIQUE NOT NULL,
    email nvarchar(100) UNIQUE NOT NULL,
    password nvarchar(20) NOT NULL,
    is_active boolean DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by nvarchar(100) DEFAULT 'SYSTEM' NOT NULL ,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by nvarchar(100)
);

--Create sequence for user table
--CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1;