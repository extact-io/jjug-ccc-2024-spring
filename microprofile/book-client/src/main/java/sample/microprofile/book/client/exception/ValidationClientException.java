package sample.microprofile.book.client.exception;

import sample.microprofile.book.client.exception.BookClientExceptionMapper.ErrorMessage;

public class ValidationClientException extends RuntimeException {

    private ErrorMessage message;

    public ValidationClientException(ErrorMessage message) {
        super(message.toString());
        this.message = message;
    }
}
