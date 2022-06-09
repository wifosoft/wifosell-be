package com.wifosell.zeus.database;

import java.io.FileNotFoundException;

public interface ISeeder {
    void prepareJpaRepository();
    void run() throws FileNotFoundException;

}
