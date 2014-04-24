package results.registration;

/**
 * Created by Ivan on 30.03.2014 in 19:44.
 */
public final class RegistrationResult {
    private RegistrationResultEnum result;
    private Integer idUser;

    public RegistrationResult(RegistrationResultEnum result, Integer idUser) {
        this.result = result;
        this.idUser = idUser;
    }

    public RegistrationResultEnum getResult() {
        return result;
    }

    public Integer getIdUser() {
        return idUser;
    }
}