# Moshi support

```groovy
implementation "io.github.ztiany:android-simple-http-moshi:2.0.1"
implementation "com.squareup.retrofit2:converter-moshi:2.11.0"
```

Create a Moshi instance with `MoshiFactory.newMoshi()` and use the same instance for Retrofit and any manual JSON parsing:

```kotlin
val moshi = MoshiFactory.newMoshi()

override fun configRetrofit(builder: Retrofit.Builder) {
    builder.addConverterFactory(MoshiConverterFactory.create(moshi))
}
```

The default factory supports Kotlin classes through `KotlinJsonAdapterFactory`. It also accepts numeric strings for `Int`, `Float`, and `Double`, converts scalar JSON values to `String`, and handles `Unit` and `Void` responses. Invalid numeric values become zero for primitive types and `null` for boxed types.

For an abstract model backed by a concrete implementation, annotate the abstract type with `@AutoMoshi(autoClass = ConcreteModel.class)`. The concrete class must be compatible with Moshi serialization.
