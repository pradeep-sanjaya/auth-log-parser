package com.pradeep;

import com.pradeep.util.LogParser;

import java.util.HashSet;

public class App
{
    public static void main( String[] args )
    {
        LogParser parser = new LogParser();
        HashSet<String> ips = parser.parse();
        parser.generateEntries(ips);
    }
}
