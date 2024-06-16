package sample.microprofile.book.client.exception;

import sample.microprofile.book.client.exception.BookClientExceptionMapper.ErrorMessage;

public class DuplicateClientException extends RuntimeException {

    private ErrorMessage message;

    public DuplicateClientException(ErrorMessage message) {
        super(message.toString());
        this.message = message;
    }
}
