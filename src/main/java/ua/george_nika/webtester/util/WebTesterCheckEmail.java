package ua.george_nika.webtester.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by George on 29.06.2015.
 */
public class WebTesterCheckEmail {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validate(final String email) {
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
