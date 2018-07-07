package matthew.won.utoronto.prod.Datatypes;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Colour {
    //Create a hashmap for colour to int
    private static ArrayList<Colour> list_of_colours = new ArrayList<>();
    private static HashMap<String, Integer> name_to_id = new HashMap<>();

    private String name;
    private Integer colour_code;

    private static boolean default_colours = false;

    public Colour () {
        this.name = "Black";
        this.colour_code = Color.BLACK;
    }

    private Colour (String colour, Integer colour_code) {
        this.name = colour;
        this.colour_code = colour_code;
        if (!name_to_id.containsKey(this.name) && !list_of_colours.contains(this)) {
            name_to_id.put(this.name, this.colour_code);
            list_of_colours.add(this);
        }
    }

    public Colour (String colour, String hex_code) {
        this.name = colour;
        this.colour_code = Color.parseColor(hex_code);
        if (!name_to_id.containsKey(this.name) && !list_of_colours.contains(this)) {
            name_to_id.put(this.name, this.colour_code);
            list_of_colours.add(this);
        }
    }


    public String getColour () {
        return name;
    }

    public Integer getColourCode () {
        return colour_code;
    }

    public static void create_default_colours () {
        if (!default_colours) {
            new Colour("White", Color.WHITE);
            new Colour("Black", Color.BLACK);
            new Colour("Red", Color.RED);
            new Colour("Yellow", Color.YELLOW);
            new Colour("Green", Color.GREEN);
            new Colour("Blue", Color.BLUE);
            new Colour("Cyan", Color.CYAN);
            new Colour("Dark Gray", Color.DKGRAY);
            new Colour("Light Gray", Color.LTGRAY);
            new Colour("Magenta", Color.MAGENTA);
            new Colour("Transparent", Color.TRANSPARENT);
            default_colours = true;
        }
    }

    public static ArrayList<Colour> give_list_of_colours () {
        create_default_colours();
        return list_of_colours;
    }

    public static Integer get_colour (String name) {
        create_default_colours();
        if (name == null) return 0;
        return name_to_id.get(name);
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
