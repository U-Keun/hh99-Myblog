package myblog.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import myblog.myblog.domain.StatusCode;

@Data
@AllArgsConstructor(staticName = "set")
public class BasicResponseDto<T> {
    private StatusCode status;
    private String message;
    private T data;

    public static <T> BasicResponseDto<T> setSuccess(String message, T data){
        return BasicResponseDto.set(StatusCode.OK, message, data);
    }

    public static <T> BasicResponseDto<T> setBadRequest(String message){
        return BasicResponseDto.set(StatusCode.BAD_REQUEST, message, null);
    }
}
