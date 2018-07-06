package matthew.won.utoronto.prod.Database;

import java.util.Arrays;

public class Datatype_SQL<dataType extends Stringable<dataType>> {

    public String TABLE_NAME;
    public String COLUMN_NAMES;
    public String[] COLUMN_NAMES_;

    private dataType type;

    public Datatype_SQL(String table_name, dataType type) {
        TABLE_NAME = table_name;
        this.type = type;
        setupTables();
    }

    private void setupTables () {
        this.COLUMN_NAMES = type.getDatabaseForum();
        COLUMN_NAMES_ = COLUMN_NAMES.split(",");
        if (COLUMN_NAMES.contains("FOREIGN"))
            COLUMN_NAMES_ = Arrays.copyOf(COLUMN_NAMES_, COLUMN_NAMES_.length - 1);
        for (int i = 0; i < COLUMN_NAMES_.length; i++) {
            String name = COLUMN_NAMES_[i].trim().split(" ")[0];
            COLUMN_NAMES_[i] = name;
        }
    }

    public dataType returnDummyVariable () {
        return type;
    }

}
