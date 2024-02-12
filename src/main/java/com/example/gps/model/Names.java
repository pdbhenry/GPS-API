package com.example.gps.model;

import java.util.Random;

public final class Names {
    public static String[] locationNames = {"7-Eleven","Aldi","Burger King","Chipotle","Fastop","Lidl","McDonald's",
            "Ruby Tuesday","Walmart","Macy's","Nordstrom","Belk","Dillard's","Big Lots","Dollar Tree",
            "Burlington","BJ's","Costco","JCPenney","Five Below","Dollar General","Kohl's","Family Dollar",
            "Dick's","Sam's Club","Target","TJ Maxx","Marshalls","BonChon Chicken","Noodles & Company",
            "Panda Express","Auntie Anne's","Cinnabon","Insomnia Cookies","Krispy Kreme","Wetzel's Pretzel's",
            "CosMc's","Dunkin' Donuts","Jamba Juice","Kung Fu Tea","Orange Julius","Smoothie King","Starbucks",
            "Tropical Smoothie","Bojangles","Chick-fil-A","Cluck-U Chicken","El Pollo Loco","Jollibee","KFC",
            "Popeyes","Baskin-Robbins","Ben & Jerry's","Braum's","Bruster's Ice Cream","Dairy Queen",
            "Dippin' Dots","Sweet Frog","A&W","Carl's Jr.","Checkers","Five Guys","In-N-Out","Jack in the Box",
            "Shake Shack","Sonic","Steak 'n Shake","Wendy's","Whataburger","White Castle","Cava","Del Taco",
            "Qdoba","Taco Bell","Taco Bueno","Domino's","Blaze Pizza","Little Caesars","Ledo Pizza","MOD Pizza",
            "Papa John's","Papa Murphy's","Pizza Hut","IKEA","Sbarro","Saladworks","Sweetgreen","Arby's",
            "Jimmy John's","Panera Bread","Quiznos","Subway","Long John Silver's","Applebee's","Bob Evans",
            "Cheddar's","Hard Rock Cafe","Shoney's","TGI Fridays","P.F. Chang's","Denny's","IHOP","Waffle House",
            "Buffalo Wild Wings","Red Robin","Wahlburgers","Chili's","The Old Spaghetti Factory","Red Lobster",
            "Cracker Barrel","Longhorn Steakhouse","Outback Steakhouse","Dave & Busters","Chuck E. Cheese"};

    public String getRandomName() {
        Random r = new Random();
        int index = r.nextInt(locationNames.length);
        return locationNames[index];
    }
}
