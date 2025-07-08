-- Script to create missing enum types for hotcinemas_be PostgreSQL database

CREATE TYPE booking_status_enum AS ENUM ('PENDING', 'CONFIRMED', 'CANCELLED', 'EXPIRED');
CREATE TYPE payment_status_enum AS ENUM ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED');
CREATE TYPE discount_type_enum AS ENUM ('PERCENTAGE', 'FIXED_AMOUNT');
CREATE TYPE room_type_enum AS ENUM ('STANDARD', 'IMAX', 'VIP');
CREATE TYPE seat_type_enum AS ENUM ('VIP', 'NORMAL', 'COUPLE');
CREATE TYPE seat_status_enum AS ENUM ('AVAILABLE', 'HELD', 'BOOKED', 'UNAVAILABLE');
CREATE TYPE audio_option_enum AS ENUM ('DUBBED', 'SUBTITLED', 'ORIGINAL');
CREATE TYPE payment_method_enum AS ENUM ('MOMO', 'VNPAY');
CREATE TYPE token_type_enum AS ENUM ('ACCESS', 'REFRESH');
