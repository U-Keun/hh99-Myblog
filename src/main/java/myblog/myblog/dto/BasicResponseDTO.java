package myblog.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import myblog.myblog.domain.StatusCode;

@Data
@AllArgsConstructor(staticName = "set")
public class BasicResponseDTO<T> {
    private StatusCode status;
    private String message;
    private T data;

    public static <T> BasicResponseDTO<T> setSuccess(String message, T data){
        return BasicResponseDTO.set(StatusCode.OK, message, data);
    }

    public static <T> BasicResponseDTO<T> setBadRequest(String message){
        return BasicResponseDTO.set(StatusCode.BAD_REQUEST, message, null);
    }
}
