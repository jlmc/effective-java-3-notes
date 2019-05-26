package io.costax.rethinking.designpatterns;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RunInOtherThreadTest {

    @Test
    @DisplayName("Execute in other thread in old way")
    void executeThread() throws InterruptedException {

        RunInOtherThread.executeThread();
    }

    @Test
    @DisplayName("Execute in other thread in new way")
    void executeThreadKillingVerbose() throws InterruptedException {

        RunInOtherThread.executeThreadKillingVerbose();
    }
}