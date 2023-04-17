package myblog.myblog.domain;

import lombok.Data;

@Data
public class Message {
    private StatusCode status;
    private String message;
    private Object data;

    public Message(StatusCode status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
