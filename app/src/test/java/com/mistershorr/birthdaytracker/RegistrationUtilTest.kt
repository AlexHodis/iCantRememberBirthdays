package com.mistershorr.birthdaytracker


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    // methodName_someCondition_expectedResult
    @Test
    fun validatePassword_emptyPassword_returnsFalse() {
        val result = RegistrationUtil.validatePassword("", "")
        // assertThat(actualValue).isEqual(desiredValue)
        assertThat(result).isFalse()
    }

    @Test
    fun validatePassword_passwordsDontMatch_returnsFalse() {

    }

    // Make tests for failures of
        // min length 8 chars
        // at least one digit
        // at least one capital letter
    // Make a test for good matching password works

    @Test
    fun validatePassword_lengthUnder8_returnsFalse() {
        val result = RegistrationUtil.validatePassword("1Abcdef",
        "1Abcdef")
        assertThat(result).isFalse()
    }

    // Once all the test are made,
    // implement the validatePassword function in the Util class
    // and run the tests to make sure that the function works

    // Make the tests for the other functions in the Util class with
    // the common failures and 1 success for each

}