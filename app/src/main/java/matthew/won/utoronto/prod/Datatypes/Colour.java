package matthew.won.utoronto.prod.Datatypes;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Stringable;

public class Colour implements Stringable<Colour> {
    public static ArrayList<Colour> list_of_colours = new ArrayList<>();

    private String id;
    private String colour;
    private String hex_code;

    Colour () {

    }

    Colour (String colour, String hex_code) {
        this.colour = colour;
        this.hex_code = hex_code;
        list_of_colours.add(this);
    }

    public String getColour () {
        return colour;
    }

    public String getHexCode () {
        return hex_code;
    }

    public ArrayList<String> stringify () {
        ArrayList<String> data = new ArrayList<String>();
        data.add(id);
        data.add(colour);
        data.add(hex_code);
        return data;
    }

    public void unstringify (ArrayList<String> data) {
        this.id = data.get(0);
        this.colour = data.get(1);
        this.hex_code = data.get(2);
    }

    public Colour newInstance() {
        return new Colour();
    }

    public String getDatabaseForum() {
        return "COLOUR TEXT, HEX TEXT";
    }
}
