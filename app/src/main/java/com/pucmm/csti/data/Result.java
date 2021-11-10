package com.pucmm.csti.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {

    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
