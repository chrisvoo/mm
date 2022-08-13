import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import utils.FileManagerModule;

import java.io.IOException;


public class GlobalSetupTeardownListener implements LauncherSessionListener {

    private Fixture fixture;

    public void launcherSessionOpened(LauncherSession session) {
        // Avoid setup for test discovery by delaying it until tests are about to be executed
        session.getLauncher().registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void testPlanExecutionStarted(TestPlan testPlan) {
                if (fixture == null) {
                    fixture = new Fixture();
                    try {
                        fixture.setUp();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void launcherSessionClosed(LauncherSession session) {
        if (fixture != null) {
            fixture.tearDown();
            fixture = null;
        }
    }

    static class Fixture {
        private Microservice microservice;

        void setUp() throws IOException {
            Injector injector = Guice.createInjector(new FileManagerModule());
            microservice = injector.getInstance(Microservice.class);
            microservice.start();
        }

        void tearDown() {
            microservice.stop();
        }
    }
}