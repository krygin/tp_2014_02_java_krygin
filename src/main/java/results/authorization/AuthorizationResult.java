package results.authorization;

/**
 * Created by Ivan on 30.03.2014.
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

    public void setResult(AuthorizationResultEnum result) {
        this.result = result;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}