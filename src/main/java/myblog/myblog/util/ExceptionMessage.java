package myblog.myblog.util;

public enum ExceptionMessage {

    NO_SUCH_MEMBER_EXCEPTION("등록된 회원이 없습니다."),
    DUPLICATE_ID_EXCEPTION("중복된 ID가 존재합니다."),
    NOT_MATCHING_PASSWORD_EXCEPTION("비밀번호가 일치하지 않습니다."),
    NO_SUCH_BOARD_EXCEPTION("게시글이 존재하지 않습니다."),
    NO_AUTHORIZATION_EXCEPTION("권한이 없습니다."),
    NO_SUCH_COMMENT_EXCEPTION("댓글이 존재하지 않습니다.");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
