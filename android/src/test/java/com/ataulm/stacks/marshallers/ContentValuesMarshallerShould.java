package com.ataulm.stacks.marshallers;

import android.content.ContentValues;

import com.ataulm.stacks.model.AndroidStack;
import com.ataulm.stacks.persistence.Stacks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class ContentValuesMarshallerShould {

    ContentValuesMarshaller marshaller;
    ContentValues mockValues = mock(ContentValues.class);

    @Before
    public void setUp() throws Exception {
        marshaller = new ContentValuesMarshaller(mockValues);
    }

    @Test
    public void addTheIdOfTheStack_whenInsertingANewStack() throws Exception {
        marshaller.valuesForInsertFrom(AndroidStack.ZERO);

        verify(mockValues).put(Stacks.ID, AndroidStack.ZERO.id);
    }

    @Test
    public void ignoreTheIdOfTheStack_whenUpdatingAnExistingStack() throws Exception {
        marshaller.valuesForUpdateFrom(AndroidStack.ZERO);

        verify(mockValues, never()).put(Stacks.ID, AndroidStack.ZERO.id);
    }

}
