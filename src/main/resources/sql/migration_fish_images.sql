START TRANSACTION;

    CREATE TABLE IF NOT EXISTS fish_images (
        id INT NOT NULL AUTO_INCREMENT,
        file_name VARCHAR(255) NOT NULL,
        fish_id INT NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT fk_fish_images_fish
            FOREIGN KEY (fish_id) REFERENCES fish(id)
            ON DELETE CASCADE
    );


    INSERT INTO fish_images (file_name, fish_id)
    SELECT f.image_file_name, f.id
    FROM fish f
    WHERE f.image_file_name IS NOT NULL
    AND f.image_file_name <> '';

    ALTER TABLE fish DROP COLUMN image_file_name;

COMMIT;
