package me.shubhamjain.codesamples.constants;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class LogsConstantsTest {

    @Test
    void testStaticMethod() {
        try(MockedStatic<LogsConstants> mockedStatic
                    = Mockito.mockStatic(LogsConstants.class)) {
            Feature feature = spy(Feature.class);
            feature.save();
            verify(feature, times(1)).write();
            verify(feature, times(1)).delete();
        }
    }
}
