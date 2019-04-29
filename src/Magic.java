import java.util.ArrayList;
import java.util.List;

class Magic {

    private String name;
    private int cost, dmg, dmg_diff;

    Magic(String name, int cost, int dmg, int dmg_diff) {
        this.name = name;
        this.cost = cost;
        this.dmg = dmg;
        this.dmg_diff = dmg_diff;
    }

    static List<Magic> make_magic_list(String magics) {
        List<Magic> temp_list = new ArrayList<>();
        for (String item : magics.split("-")) {

            switch (item) {
                case "fire":
                    temp_list.add(new Magic("Fireball", 23, 27, 8));
                    break;

                case "ice":
                    temp_list.add(new Magic("Ice Blow", 8, 10, 4));
                    break;

                case "lightning":
                    temp_list.add(new Magic("Deadly Lightning", 17, 21, 3));
                    break;
            }
        }
        return temp_list;
    }

    String getName() {
        return name;
    }

    int getCost() {
        return cost;
    }

    int getDmg() {
        return dmg;
    }

    int getDmg_diff() {
        return dmg_diff;
    }

    int generate_dmg() {
        int lowest_dmg = this.getDmg() - this.getDmg_diff();
        int highest_dmg = this.getDmg() + this.getDmg_diff();

        return (int) Math.floor((Math.random() * (highest_dmg - lowest_dmg)) + lowest_dmg);
    }
}
