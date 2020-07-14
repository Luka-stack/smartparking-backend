package pl.ttpsc.smartparking.error.exception;

public enum ErrorCode {

    PLATE_NOT_FOUND(1001), ACCESS_NOT_FOUND(1002),
    PLATE_INVALID_INPUT(2001), ACCESS_INVALID_INPUT(2002);

    int statusCode;

    ErrorCode(int statusCode) {
        this.statusCode = statusCode;
    }

    String getByStatusCode(int statusCode) {

        for (ErrorCode error : ErrorCode.values()) {
            if (error.statusCode == statusCode) {
                return error.name();
            }
        }

        throw new NotFoundErrorCodeException("Error code '"+statusCode+"' not found");
    }
}
