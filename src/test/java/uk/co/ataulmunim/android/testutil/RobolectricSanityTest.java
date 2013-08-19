package uk.co.ataulmunim.android.testutil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertFalse;


@RunWith(RobolectricGradleTestRunner.class)
public class RobolectricSanityTest {
    @Test
    public void robolectricShadowApplicationShouldNotBeNull() throws Exception {
        ShadowApplication shadowApplication = Robolectric.getShadowApplication();
        assertFalse(shadowApplication == null);
    }
}
