package com.pradeep.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    final static Logger logger = Logger.getLogger(GeoParser.class);
    final static String AUTH_LOG_PATH = "auth.log";
    final static String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    final static String WHITELIST = "Sri Lanka";

    ClassLoader classLoader = getClass().getClassLoader();
    GeoParser geoParser = new GeoParser();

    public HashSet<String> parse() {
        HashSet<String> ips = new HashSet<String>();
        File file = new File(classLoader.getResource(AUTH_LOG_PATH).getFile());
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                ips.add(getIp(text));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }

        return ips;
    }

    private String getIp(String text) {
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else{
            return "0.0.0.0";
        }
    }

    public void generateEntries(HashSet<String> ips) {
        Iterator<String> iterator = ips.iterator();

        while (iterator.hasNext()) {

            String ip = iterator.next();

            String country =  geoParser.getCountryByIp(ip);

            if (country != null && !country.equals(WHITELIST)) {
                StringBuilder sb = new StringBuilder();
                sb.append("sudo iptables -A INPUT -s ");
                sb.append(ip);
                sb.append(" -j DROP");
                logger.info(sb);
            } else {
                logger.error("country not found, or from " + WHITELIST);
            }
        }
    }
}
