-- liquibase formatted sql
-- changeset iryna:009

UPDATE users
SET
    password_hash = '$2a$12$ZkOLIJH9xR0jeTi3YelB1uwHQupQJuuqhgwhE.lQ90zNfHBOy53oq'
WHERE
    user_id = 1;

UPDATE users
SET
    password_hash = '$2a$12$wJIhvOy9kUIUUEptpXyXzeBxh0ZN61/v9rKCgM6XV/59cMgznOcXG'
WHERE
    user_id = 2;

UPDATE users
SET
    password_hash = '$2a$12$VYRWC6Np01HnYyAvTiUa3uQdjSqmU4.aSdoToxek2R/0vDvhE.mri'
WHERE
    user_id = 3;

UPDATE users
SET
    password_hash = '$2a$12$szp/Y0q.3HO4N6EdL0qrAeiUbHAH/ONJzWk.vsGjTnKujeoWCQ0GC'
WHERE
    user_id = 4;

UPDATE users
SET
    password_hash = '$2a$12$UmK2TKq9URh21WY4yNfBX.5JYzKz6eMAgkFioDks5RRfpOoMV8hyW'
WHERE
    user_id = 5;

UPDATE users
SET
    password_hash = '$2a$12$GWqZn6HoVnX8d0Jn.BHoxe3/0gVBxh5aWV3ApyFSp5x48ZARTUx7m'
WHERE
    user_id = 6;

UPDATE users
SET
    password_hash = '$2a$12$JoSktOr4JA8ktxD/UZb/M.QIuxa87Y4M1S7an/A1CnXeSNoiBNvWa'
WHERE
    user_id = 7;

UPDATE users
SET
    password_hash = '$2a$12$ZpZcThLJ3WwAMlKSyfe9weCGRnMot.SVKS9MX6a3yTjT/UQQPgPsG'
WHERE
    user_id =  8;