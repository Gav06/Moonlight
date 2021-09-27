package dev.moonlight.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// used for marking methods that should not be called within the client, but only by minecraft / forge
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ApiCall {
}
