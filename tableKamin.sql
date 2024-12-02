DROP TABLE IF EXISTS certificates CASCADE;

-- Create the table
CREATE TABLE certificates (
    nomor_sertifikat varchar(255) PRIMARY KEY,  -- Auto-incrementing primary key
    panjang_sertifikat varchar(255) NOT NULL, --Panjang dari sertifikat
	lebar_sertifikat varchar(255) NOT NULL, --lebar dari sertifikat
    hash_value VARCHAR(256) NOT NULL  -- Hash value stored as a 64-character string
);
