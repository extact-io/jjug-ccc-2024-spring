package sample.microprofile.book.client.exception;

import sample.microprofile.book.client.exception.BookClientExceptionMapper.ErrorMessage;

public class NotFoundClientException extends RuntimeException {

    private ErrorMessage message;

    public NotFoundClientException(ErrorMessage message) {
        super(message.toString());
        this.message = message;
    }
}
