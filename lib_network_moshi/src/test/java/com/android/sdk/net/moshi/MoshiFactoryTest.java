package com.android.sdk.net.moshi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.squareup.moshi.Moshi;

import org.junit.Test;

import java.io.IOException;

import kotlin.Unit;

public class MoshiFactoryTest {

    private final Moshi moshi = MoshiFactory.newMoshi();

    @Test
    public void convertsScalarValues() throws IOException {
        assertEquals(Integer.valueOf(12), moshi.adapter(int.class).fromJson("\"12\""));
        assertEquals(Float.valueOf(1.5F), moshi.adapter(Float.class).fromJson("\"1.5\""));
        assertEquals(Double.valueOf(2.5D), moshi.adapter(Double.class).fromJson("2.5"));
        assertEquals("42", moshi.adapter(String.class).fromJson("42"));
    }

    @Test
    public void serializesModelsAndScalars() {
        ConcreteModel model = new ConcreteModel();
        model.name = "Alien";

        assertEquals("{\"name\":\"Alien\"}", moshi.adapter(AbstractModel.class).toJson(model));
        assertEquals("\"42\"", moshi.adapter(String.class).toJson("42"));
    }

    @Test
    public void usesPrimitiveFallbacksForInvalidNumbers() throws IOException {
        assertEquals(Integer.valueOf(0), moshi.adapter(int.class).fromJson("\"invalid\""));
        assertEquals(Float.valueOf(0F), moshi.adapter(float.class).fromJson("{}"));
        assertNull(moshi.adapter(Integer.class).fromJson("\"invalid\""));
        assertNull(moshi.adapter(Double.class).fromJson("null"));
    }

    @Test
    public void supportsEmptyResponseTypes() throws IOException {
        assertSame(Unit.INSTANCE, moshi.adapter(Unit.class).fromJson("{\"ignored\":true}"));
        assertNull(moshi.adapter(Void.class).fromJson("{\"ignored\":true}"));
    }

    @Test
    public void mapsAnnotatedAbstractModel() throws IOException {
        AbstractModel model = moshi.adapter(AbstractModel.class).fromJson("{\"name\":\"Alien\"}");
        assertTrue(model instanceof ConcreteModel);
        assertEquals("Alien", ((ConcreteModel) model).name);
    }

    @AutoMoshi(autoClass = ConcreteModel.class)
    public abstract static class AbstractModel {
    }

    public static class ConcreteModel extends AbstractModel {
        public String name;
    }
}
