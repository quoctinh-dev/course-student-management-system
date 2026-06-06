package ra.cms.utils;
import at.favre.lib.crypto.bcrypt.BCrypt;

public final class BCryptUtil {
    private static final int COST = 12;
    private BCryptUtil()
    {

    }
    public static String hash( String password)
    {
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }
    public static  boolean verify(String rawPassword, String hashPassword )
    {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashPassword.toCharArray());
        return result.verified;
    }
}
