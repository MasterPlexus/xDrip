package com.eveningoutpost.dexdrip.Models;

import android.provider.BaseColumns;
import android.text.format.DateFormat;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.sql.SQLException;
import java.util.List;

@Table(name = "Libre2Sensors", id = BaseColumns._ID)
public class Libre2Sensor extends PlusModel {
    static final String TAG = "Libre2Sensor";

    static final String[] schema = {
            "DROP VIEW IF EXISTS Libre2Sensors;",
            "CREATE VIEW Libre2Sensors AS SELECT MIN(_id) as _id, serial, MIN(ts) as ts_from, MAX(ts) AS ts_to, COUNT(*) AS readings FROM Libre2RawValue2 GROUP BY serial ORDER BY ts DESC;"
    };

    @Column(name = "serial", index = true)
    public String serial;

    @Column(name = "ts_from", index = false)
    public long ts_from;

    @Column(name = "ts_to", index = false)
    public long ts_to;

    @Column(name = "readings", index = false)
    public long readings;

    public static String Libre2Sensors() {
        String Sum="";

        List<Libre2Sensor> rs = new Select()
                .from(Libre2Sensor.class)
                //.where("ts_from > 0")
                .execute();
        //UserError.Log.e (TAG, rs.toString());

        for (Libre2Sensor Sensorpart : rs ) {
            Sum = Sum + Sensorpart.serial +
                    "\n" + DateFormat.format("dd.MM.yy",Sensorpart.ts_from) +
                    " to: " + DateFormat.format("dd.MM.yy",Sensorpart.ts_to) +
                    " (" +JoH.niceTimeScalarShortWithDecimalHours(Sensorpart.ts_to - Sensorpart.ts_from) + ")" +
                    " readings: " + (((Sensorpart.readings *100)/((Sensorpart.ts_to - Sensorpart.ts_from)))/600) + "%\n" +
                    "------------------\n";
        }
        return Sum;
    }

    public static void updateDB() {
        fixUpTable(schema, false);
    }
}
