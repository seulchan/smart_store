package me.smartstore.project.exception;

import me.smartstore.project.util.Message;

public class InputRangeException extends Exception {

    public InputRangeException() {
        super(Message.ERR_MSG_INVALID_INPUT_RANGE);
    }

    public InputRangeException(String message) {
        super(message);
    }
}
