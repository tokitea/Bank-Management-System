package com.jmc.mazebank;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

public abstract class BaseClass {

    @BeforeAll
    static void initializeJavaFX()throws TimeoutException {
        // Ensure the JavaFX platform is initialized once for all tests
        Platform.startup(() -> {});
        FxToolkit.registerPrimaryStage();

    }

    @AfterEach
    void cleanupJavaFX() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

}
