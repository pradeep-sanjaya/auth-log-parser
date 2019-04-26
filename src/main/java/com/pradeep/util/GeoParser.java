package com.pradeep.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetAddress;

public class GeoParser {

    final static Logger logger = Logger.getLogger(GeoParser.class);

    private String DB_PATH = "GeoLite2-Country.mmdb";
    File database;
    DatabaseReader dbReader;
    ClassLoader classLoader = getClass().getClassLoader();

    GeoParser() {
        try {
            database = new File(classLoader.getResource(DB_PATH).getFile());
            dbReader = new DatabaseReader.Builder(database).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCountryByIp(String ip) {
        String countryName = "";
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = dbReader.country(ipAddress);
            countryName = response.getCountry().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countryName;
    }

}
