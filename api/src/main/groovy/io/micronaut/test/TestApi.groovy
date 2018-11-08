package io.micronaut.test

import javax.annotation.Nullable

interface TestApi {

    String testMethod(String testString, @Nullable String otherTestString)

}
