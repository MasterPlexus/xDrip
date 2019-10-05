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
            "CREATE VIEW Libre2Sensors AS SELECT 0 as _id, serial, MIN(ts) as ts_from, MAX(ts) AS ts_to, COUNT(*) AS readings FROM Libre2RawValue2 GROUP BY serial ORDER BY ts DESC;"
    };

    @Column(name = "serial", index = true)
    public String serial;

    @Column(name = "ts_from", index = true)
    public long ts_from;

    @Column(name = "ts_to", index = true)
    public long ts_to;

    @Column(name = "readings", index = true)
    public long readngs;

    public static String Libre2Sensors() throws SQLException {
        String Sum="";
        UserError.Log.e(TAG, "start read LibreSensors");
        try {
            UserError.Log.e(TAG, "start query");
            List<Libre2Sensor> rs = new Select()
                    .from(Libre2Sensor.class)
                    .where("ts_from > 0")
                    .execute();
            UserError.Log.e(TAG, "start while num " + rs.size());
            for (Libre2Sensor rsSensor : rs ) {


                UserError.Log.e(TAG, "start sum");
                Sum += rsSensor.serial +
                        " from: " + DateFormat.format("dd.MM.yy",rsSensor.ts_from) +
                        " to: " + DateFormat.format("dd.MM.yy",rsSensor.ts_to) +
                        "\n";
            }

        } catch (Exception e) {
            UserError.Log.wtf(TAG, "LibreSensors exception" + e.toString());
        }
        UserError.Log.e(TAG, "start return");
        return Sum;
    }

    public static void updateDB() {
        fixUpTable(schema, false);
    }
}
