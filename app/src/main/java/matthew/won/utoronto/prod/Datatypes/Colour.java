package matthew.won.utoronto.prod.Datatypes;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Stringable;

public class Colour implements Stringable<Colour> {
    private static ArrayList<Colour> list_of_colours = new ArrayList<>();

    private String id;
    private String colour;
    private String hex_code;

    private static boolean default_colours = false;

    public Colour () {
        this.colour = "Black";
        this.hex_code = "000000";
    }

    public Colour (String colour, String hex_code) {
        this.colour = colour;
        this.hex_code = hex_code;
        if (!list_of_colours.contains(this))
            list_of_colours.add(this);
    }

    public String getColour () {
        return colour;
    }

    public String getHexCode () {
        return hex_code;
    }

    public static ArrayList<Colour> give_list_of_colours () {
        if (!default_colours)
            new Colour("White", "FFFFFF");
        new Colour("Black", "000000");
        new Colour("Red", "FF0000");
        new Colour("Yellow", "FFFF00");
        new Colour("Green", "00FF00");
        new Colour("Blue", "0000FF");
        default_colours = true;

        return list_of_colours;
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
