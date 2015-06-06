package com.amine.horaires.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Utils {
    public static URL generateUrlForTextLocation(String name, String location) {
        try {
            return new URL(Configuration.getAPIUrl() + "/api?key="
                    + Configuration.key + "&h=" + Configuration.hashtag
                    + "&get=shops" + "&loc=" + URLEncoder.encode(location, "ISO-8859-1")
                    + "&name=" + URLEncoder.encode(name, "ISO-8859-1"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL generateUrlForLatLng(String name, String lat,
                                           String lng) {
        try {

            return new URL(Configuration.getAPIUrl() + "/api?key="
                    + Configuration.key + "&h=" + Configuration.hashtag
                    + "&get=shops" + "&lng=" + lng + "&lat=" + lat + "&name="
                    + URLEncoder.encode(name, "ISO-8859-1") + "&order=distance");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL generateUrlForId(int idPoi) {
        try {

            return new URL(Configuration.getAPIUrl() + "/api?key="
                    + Configuration.key + "&h=" + Configuration.hashtag
                    + "&get=shop" + "&id=" + idPoi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL generateUrlForEdit(int idPoi, String periodsString) {
        try {

            return new URL(Configuration.getAPIUrl() + "/api?key="
                    + Configuration.key + "&h=" + Configuration.hashtag
                    + "&get=edit" + "&id=" + idPoi + periodsString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
