package matthew.won.utoronto.prod.Datatypes;

import android.graphics.Color;

import java.util.ArrayList;

public class Colour {
    private static ArrayList<Colour> list_of_colours = new ArrayList<>();

    private String colour_name;
    private Integer colour_code;

    private static boolean default_colours = false;

    public Colour () {
        this.colour_name = "Black";
        this.colour_code = Color.BLACK;
    }

    public Colour (String colour, Integer hex_code) {
        this.colour_name = colour;
        this.colour_code = hex_code;
        if (!list_of_colours.contains(this))
            list_of_colours.add(this);
    }

    public String getColour () {
        return colour_name;
    }

    public Integer getColourCode () {
        return colour_code;
    }

    public static ArrayList<Colour> give_list_of_colours () {
        if (!default_colours) {
            new Colour("White", Color.WHITE);
            new Colour("Black", Color.BLACK);
            new Colour("Red", Color.RED);
            new Colour("Yellow", Color.YELLOW);
            new Colour("Green", Color.GREEN);
            new Colour("Blue", Color.BLUE);
            default_colours = true;
        }

        return list_of_colours;
    }

//    public ArrayList<String> stringify () {
//        ArrayList<String> data = new ArrayList<String>();
//        data.add(id);
//        data.add(colour);
//        data.add(hex_code);
//        return data;
//    }
//
//    public void unstringify (ArrayList<String> data) {
//        this.id = data.get(0);
//        this.colour = data.get(1);
//        this.hex_code = data.get(2);
//    }
//
//    public Colour newInstance() {
//        return new Colour();
//    }
//
//    public String getDatabaseForum() {
//        return "COLOUR TEXT, HEX TEXT";
//    }
}
