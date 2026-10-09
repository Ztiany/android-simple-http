package com.android.sdk.net.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import kotlin.Unit;
import timber.log.Timber;

/** Scalar adapters shared by the default Moshi configuration. */
final class ScalarJsonAdapters {

    static final JsonAdapter<Unit> UNIT = new JsonAdapter<Unit>() {
        @Override
        public Unit fromJson(JsonReader reader) throws IOException {
            reader.skipValue();
            return Unit.INSTANCE;
        }

        @Override
        public void toJson(JsonWriter writer, Unit value) throws IOException {
            writer.nullValue();
        }
    };

    static final JsonAdapter<Void> VOID = new JsonAdapter<Void>() {
        @Override
        public Void fromJson(JsonReader reader) throws IOException {
            reader.skipValue();
            return null;
        }

        @Override
        public void toJson(JsonWriter writer, Void value) throws IOException {
            writer.nullValue();
        }
    };

    private ScalarJsonAdapters() {
    }

    static void addTo(Moshi.Builder builder) {
        builder.add(int.class, new NumberJsonAdapter<>(ScalarJsonAdapters::asInteger, 0));
        builder.add(Integer.class, new NumberJsonAdapter<>(ScalarJsonAdapters::asInteger, null));
        builder.add(float.class, new NumberJsonAdapter<>(ScalarJsonAdapters::asFloat, 0F));
        builder.add(Float.class, new NumberJsonAdapter<>(ScalarJsonAdapters::asFloat, null));
        builder.add(double.class, new NumberJsonAdapter<>(ScalarJsonAdapters::asDouble, 0D));
        builder.add(Double.class, new NumberJsonAdapter<>(ScalarJsonAdapters::asDouble, null));
        builder.add(String.class, new JsonAdapter<String>() {
            @Override
            public String fromJson(JsonReader reader) throws IOException {
                switch (reader.peek()) {
                    case STRING:
                    case NUMBER:
                        return reader.nextString();
                    case BOOLEAN:
                        return Boolean.toString(reader.nextBoolean());
                    default:
                        reader.skipValue();
                        return null;
                }
            }

            @Override
            public void toJson(JsonWriter writer, String value) throws IOException {
                writer.value(value);
            }
        });
    }

    private static Integer asInteger(Object value) {
        return value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
    }

    private static Float asFloat(Object value) {
        return value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(value.toString());
    }

    private static Double asDouble(Object value) {
        return value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(value.toString());
    }

    private interface NumberParser<T extends Number> {
        T parse(Object value);
    }

    private static final class NumberJsonAdapter<T extends Number> extends JsonAdapter<T> {

        private final NumberParser<T> parser;
        private final T fallback;

        private NumberJsonAdapter(NumberParser<T> parser, T fallback) {
            this.parser = parser;
            this.fallback = fallback;
        }

        @Override
        public T fromJson(JsonReader reader) throws IOException {
            Object value = reader.readJsonValue();
            if (value == null) {
                return fallback;
            }
            try {
                return parser.parse(value);
            } catch (NumberFormatException exception) {
                Timber.e(exception, "Cannot parse JSON number: %s", value);
                return fallback;
            }
        }

        @Override
        public void toJson(JsonWriter writer, T value) throws IOException {
            writer.value(value);
        }
    }
}
