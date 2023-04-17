package myblog.myblog.dto;

import lombok.Data;
import myblog.myblog.domain.StatusCode;

@Data
public class BasicResponseDTO {
    private StatusCode status;
    private String message;
    private Object data;

    public BasicResponseDTO(StatusCode status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
