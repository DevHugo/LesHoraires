package com.amine.horaires.util;

import com.amine.horaires.models.Shop;

public class Configuration {
    // Cles necessaires pour l'api lesHorraires
    public static final String key = "REPLACE_THIS_WITH_KEY";
    public static final String hashtag = "REPLACE_THIS_WITH_A_HASHTAG";
    public static String pays = "FR";
    public static Shop currentShop = null;

    public static String getAPIUrl() {
        if (pays.equals("FR"))
            return "http://www.les-horaires.fr";
        else if (pays.equals("GB"))
            return "http://www.shopping-time.co.uk";
        else if (pays.equals("US"))
            return "http://www.shopping-time.com";

        return "http://www.les-horaires.fr";
    }

}
