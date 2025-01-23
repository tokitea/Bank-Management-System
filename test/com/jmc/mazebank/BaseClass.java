package com.jmc.mazebank;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseClass {

    @BeforeAll
    static void initializeJavaFX() {
        // Ensure the JavaFX platform is initialized once for all tests
        Platform.startup(() -> {});
    }

}
