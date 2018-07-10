package matthew.won.utoronto.prod.Database;

import java.util.ArrayList;

public interface Stringable<dataType> {
    // Be able to turn all of its parameters into strings
    ArrayList<String> stringify();

    // Be able to turn a table of strings into the datatype
    void unstringify(ArrayList<String> data);

    // Create a new instance of the datatype
    dataType newInstance();

    String getDatabaseForum();

}