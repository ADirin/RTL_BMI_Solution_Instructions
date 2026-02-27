CREATE TABLE IF NOT EXISTS bmi_results (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           weight DOUBLE NOT NULL,
                                           height DOUBLE NOT NULL,
                                           bmi DOUBLE NOT NULL,
                                           language VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );