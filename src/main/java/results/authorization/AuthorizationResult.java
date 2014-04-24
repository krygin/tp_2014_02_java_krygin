package results.authorization;

/**
 * Created by Ivan on 30.03.2014 in 19:42.
 */
final public class AuthorizationResult {
    private AuthorizationResultEnum result;
    private Integer idUser;

    public AuthorizationResult(AuthorizationResultEnum result, Integer idUser) {
        this.result = result;
        this.idUser = idUser;
    }

    public AuthorizationResultEnum getResult() {
        return result;
    }

    public Integer getIdUser() {
        return idUser;
    }
}