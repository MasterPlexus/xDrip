package com.eveningoutpost.dexdrip.Models;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "Libre2RawValue")
public class Libre2RawValue extends PlusModel {

    static final String[] schema = {
            "CREATE TABLE Libre2RawValue (ts INTEGER PRIMARY KEY);",
            "ALTER TABLE Libre2RawValue ADD COLUMN Id INTEGER;",
            "ALTER TABLE Libre2RawValue ADD COLUMN serial STRING;",
            "ALTER TABLE Libre2RawValue ADD COLUMN glucose REAL;",

            "CREATE INDEX index_Libre2RawValue_timestamp on Libre2RawValue(ts);"
    };

    @Column(name = "serial", index = true)
    public String serial;

    @Column(name = "ts", index = true)
    public long timestamp;

    @Column(name = "glucose", index = false)
    public double glucose;

    public static List<Libre2RawValue> last20Minutes() {
        double timestamp = (new Date().getTime()) - (60000 * 20);
        return new Select()
                .from(Libre2RawValue.class)
                .where("ts >= " + timestamp)
                .orderBy("ts asc")
                .execute();
    }

    public static List<Libre2RawValue> latestForGraph(int number, double startTime) {
        return latestForGraph(number, (long) startTime, Long.MAX_VALUE);
    }

    public static List<Libre2RawValue> latestForGraph(int number, long startTime) {
        return latestForGraph(number, startTime, Long.MAX_VALUE);
    }

    public static List<Libre2RawValue> latestForGraph(int number, long startTime, long endTime) {
        return new Select()
                .from(Libre2RawValue.class)
                .where("ts >= " + Math.max(startTime, 0))
                .where("ts <= " + endTime)
                .where("glucose != 0")
                .orderBy("ts desc")
                .limit(number)
                .execute();
    }

    public static void updateDB() {
        fixUpTable(schema, false);
    }
}