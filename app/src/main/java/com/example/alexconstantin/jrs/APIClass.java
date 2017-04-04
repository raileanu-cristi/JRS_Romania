package com.example.alexconstantin.jrs;

public class APIClass {

    private String objectives="{"
            + "  \"objectives\": ["
            + "    {"
            + "      \"id\": \"3\","
            + "      \"name\": \"Objectve1\","
            + "      \"category\" : \"25\","
            + "      \"latitude\" : \"44\","
            + "      \"longitude\" : \"26\""
            + "    },"
            + "    {"
            + "      \"id\": \"1\","
            + "      \"name\": \"Objective2\","
            + "      \"category\" : \"25\","
            + "      \"latitude\" : \"37.33774833333334\","
            + "      \"longitude\" : \"-121.88670166666667\""
            + "    },"
            + "    {"
            + "      \"id\": \"2\","
            + "      \"name\": \"Objective3\","
            + "      \"category\" : \"37\","
            + "      \"latitude\" : \"32\","
            + "      \"longitude\" : \"88\""
            + "    }"
            + "  ]"
            + "}";

    private String categories="{"
            + "  \"categories\": ["
            + "    {"
            + "      \"id\": \"1\","
            + "      \"name\": \"Birouri imigrări\""
            + "    },"
            + "    {"
            + "      \"id\": \"2\","
            + "      \"name\": \"Servicii socio-medicale\""
            + "    },"
            + "    {"
            + "      \"id\": \"3\","
            + "      \"name\": \"Servicii gratuite ONG\""
            + "    },"
            + "    {"
            + "      \"id\": \"4\","
            + "      \"name\": \"Locații divertisment\""
            + "    },"
            + "    {"
            + "      \"id\": \"5\","
            + "      \"name\": \"Atracții turistice\""
            + "    },"
            + "    {"
            + "      \"id\": \"6\","
            + "      \"name\": \"Altele\""
            + "    }"
            + "  ]"
            + "}";

    public String getObjectives()
    {
        return this.objectives;
    }

    public String getCategories()
    {
        return this.categories;
    }

}
