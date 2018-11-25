package com.eveningoutpost.dexdrip.watch.lefun;

// jamorham

import static com.eveningoutpost.dexdrip.Models.JoH.emptyString;

public class ModelFeatures {

    static int getScreenWidth() {
        return getScreenWidth(LeFun.getModel());
    }

    static int getScreenWidth(final String model) {

        if (emptyString(model)) return 4; // unknown default

        switch (model) {

            case "F3S":
                return 7;

            case "F3":          // doesn't id by string
                return 10;

            case "F12":
            default:
                return 4;


        }
    }

}
