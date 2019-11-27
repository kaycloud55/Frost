package com.kaycloud.framework.util

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class Preconditions {

    companion object {


        /**
         * Ensures that an object reference passed as a parameter to the calling
         * method is not null.
         *
         * @param reference an object reference
         * @return the non-null reference that was validated
         * @throws NullPointerException if `reference` is null
         */
        fun <T> checkNotNull(reference: T?): T {
            if (reference == null) {
                throw NullPointerException()
            }
            return reference
        }

        /**
         * Ensures that an object reference passed as a parameter to the calling
         * method is not null.
         *
         * @param reference an object reference
         * @param errorMessage the exception message to use if the check fails; will
         * be converted to a string using [String.valueOf]
         * @return the non-null reference that was validated
         * @throws NullPointerException if `reference` is null
         */
        fun <T> checkNotNull(reference: T?, errorMessage: Any): T {
            if (reference == null) {
                throw Throwable(errorMessage.toString())
            }
            return reference
        }
    }
}