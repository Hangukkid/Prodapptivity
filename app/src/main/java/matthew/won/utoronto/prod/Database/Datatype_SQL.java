package matthew.won.utoronto.prod.Database;

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
        for (int i = 0; i < COLUMN_NAMES_.length; i++) {
            COLUMN_NAMES_[i] = COLUMN_NAMES_[i].trim().split(" ")[0];
        }
    }

    public dataType returnDummyVariable () {
        return type;
    }

}
