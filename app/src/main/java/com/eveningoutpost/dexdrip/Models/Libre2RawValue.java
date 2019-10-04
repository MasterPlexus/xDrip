package com.eveningoutpost.dexdrip.Models;


        import android.provider.BaseColumns;
        import android.text.format.DateFormat;

        import com.activeandroid.Model;
        import com.activeandroid.annotation.Column;
        import com.activeandroid.annotation.Table;
        import com.activeandroid.query.Select;

        import java.sql.Array;
        import java.sql.ResultSet;
        import java.sql.ResultSetMetaData;
        import java.sql.SQLException;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

@Table(name = "Libre2RawValue2", id = BaseColumns._ID)
public class Libre2RawValue extends PlusModel {

    static final String[] schema = {
            "DROP TABLE Libre2RawValue;",
            "CREATE TABLE Libre2RawValue2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, ts INTEGER, serial STRING, glucose REAL);",
            "CREATE INDEX index_Libre2RawValue2_ts on Libre2RawValue2(ts);"
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

    public static Libre2RawValue lastReading() {
        List<Libre2RawValue> Result;
        double timestamp = (new Date().getTime()) - (60000 * 20);
        Result = new Select()
                .from(Libre2RawValue.class)
                .where("ts >= " + timestamp)
                .orderBy("ts desc")
                .limit(1)
                .execute();

        return Result.get(0);
    }

    public static String Libre2Sensors() throws SQLException {
        String Sum="";
        UserError.Log.d("Libre2RawValue", "start read LibreSensors");
        try {
            UserError.Log.d("Libre2RawValue", "start query");
            List<Libre2Sensor> rs = new Select("serial, MIN(ts) as ts_from, MAX(ts) AS ts_to")
                    .from(Libre2Sensor.class)
                    .groupBy("serial")
                    .orderBy("ts ASC")
                    .limit(10)
                    .execute();
            UserError.Log.d("Libre2RawValue", "start while");
            for (Libre2Sensor rsSensor : rs ) {


                UserError.Log.d("Libre2RawValue", "start sum");
                Sum += rsSensor.serial + " from: " + DateFormat.format("dd.MM.yyyy",rsSensor.ts_from) + " to: " + DateFormat.format("dd.MM.yyyy",rsSensor.ts_to) +"\n";
            }

        } catch (Exception e) {
            UserError.Log.wtf("Libre2RawValue", "LibreSensors exception" + e.toString());
        }
        UserError.Log.d("Libre2RawValue", "start return");
        return Sum;
    }

    public static void updateDB() {
        fixUpTable(schema, false);
    }
}

class Libre2Sensor extends PlusModel {

    @Column(name = "serial", index = true)
    public String serial;

    @Column(name = "ts_from", index = true)
    public long ts_from;

    @Column(name = "ts_to", index = true)
    public long ts_to;

}