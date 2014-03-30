/**
 * Created by Ivan on 30.03.2014.
 */
final public class RegistrationResult {
    private RegistrationResultEnum result;
    private Integer idUser;

    public RegistrationResult(RegistrationResultEnum result, Integer idUser) {
        this.result = result;
        this.idUser = idUser;
    }

    public RegistrationResultEnum getResult() {
        return result;
    }

    public void setResult(RegistrationResultEnum result) {
        this.result = result;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}