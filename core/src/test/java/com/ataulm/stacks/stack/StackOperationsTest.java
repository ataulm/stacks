package com.ataulm.stacks.stack;

import com.squareup.moshi.JsonAdapter;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class StackOperationsTest {

    StackOperations stackOperations;

    @Mock
    JsonStackConverter mockJsonStackConverter;

    @Mock
    JsonRepository mockJsonRepository;

    @Mock
    JsonAdapter<List<JsonStack>> mockJsonAdapter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        stackOperations = new StackOperations(mockJsonRepository, mockJsonAdapter, mockJsonStackConverter);
    }

    @Test
    public void given_FOO_when_BAR_then_ZOD() {
        stackOperations.getStacks();

        verify(mockJsonRepository).getStacks();
    }

}
