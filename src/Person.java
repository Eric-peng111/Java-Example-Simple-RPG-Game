import java.util.List;
import java.util.Scanner;

class Person {

    private String name;
    private int hp, hp_max, mp, mp_max, dmg, dmg_diff;
    private List<Magic> magics;
    private int block_probability = 5;
    private Scanner scanner;

//    == Constructor method
    Person(String name, int hp, int mp, int dmg, int dmg_diff, List<Magic> magics) {
        this.name = name;
        this.hp = hp;
        this.hp_max = hp;
        this.mp = mp;
        this.mp_max = mp;
        this.dmg = dmg;
        this.dmg_diff = dmg_diff;
        this.magics = magics;
        this.scanner = new Scanner(System.in);
    }

//    == Static method for create objects
//    enemy_code:
//    1 for Light Warrior
//    2 for Heavy Warrior
//    3 for General
//    4 for Boss
    static Person make_an_enemy(int enemy_code) {
        switch (enemy_code) {

            case 1:
                return new Person("Light Warrior", 70, 40, 16, 9, Magic.make_magic_list("lightning-ice"));

            case 2:
                return new Person("Heavy Warrior", 110, 55, 27, 4, Magic.make_magic_list("fire-ice"));

            case 3:
                return new Person("General", 150, 65, 32, 3, Magic.make_magic_list("fire-lightning"));

            case 4:
                return new Person("Boss", 300, 80, 36, 3, Magic.make_magic_list("lightning-ice-fire"));

            default:
                return null;
        }
    }

//    ===== Getter/Setter START =====
//    =====
    int getBlock_probability() {
        return block_probability;
    }

    void setBlock_probability(int block_probability) {
        this.block_probability = block_probability;
    }

    String getName() {
        return name;
    }

    int getHp() {
        return hp;
    }

    int getHp_max() {
        return hp_max;
    }

    int getMp() {
        return mp;
    }

    int getMp_max() {
        return mp_max;
    }

    int getDmg() {
        return dmg;
    }

    int getDmg_diff() {
        return dmg_diff;
    }

    List<Magic> getMagics() {
        return magics;
    }
//    =====
//    ===== Getter/Setter END =====

//    == Non-Static Method == Generate Damage
    int generate_dmg() {
        int lowest_dmg = this.getDmg() - this.getDmg_diff();
        int highest_dmg = this.getDmg() + this.getDmg_diff();

        return (int) Math.round((Math.random() * (highest_dmg - lowest_dmg)) + lowest_dmg);
    }

//    == Non-Static Method == Take Damage
    int take_damage(int damage) {

        if ((Math.random() * 100) < block_probability) {
//            System.out.println(String.format("%s blocked attack.", this.name));
            return 0;

        } else {
            if ((this.hp - damage) <= 0) {
                int damage_done = this.hp;
                this.hp = 0;
                return damage_done;
            } else {
                this.hp -= damage;
                return damage;
            }
        }

    }

//    == Non-Static Method == Choose an Action
//    1->attack, 2->magic, 3->block
    int choose_an_action() {
        String player_choice = "";

        while (true) {
            System.out.print(String.format("%s will do an action:\n1. Attack\n2. Magic\n3. Block\nChoose an action: ", this.name));
            player_choice = scanner.nextLine();

            if ((player_choice.equals("1")) || (player_choice.equals("2")) || (player_choice.equals("3"))) {

                return Integer.valueOf(player_choice);

            } else {
                System.out.println("-- (Error) Please choose correctly. (1, 2 or 3)");
            }

        }
    }

//    == Non-Static Method == Choose a Magic
    Magic choose_a_magic() {
        System.out.println(String.format("%s will cast a magic:", this.name));
        int magic_counter = 0;

        for (Magic magic : this.getMagics()) {
            System.out.print(magic_counter + 1);
            System.out.println(String.format(
                    ". %s (DMG %s-%s) (COST %s)",
                    magic.getName(),
                    magic.getDmg() - magic.getDmg_diff(),
                    magic.getDmg() + magic.getDmg_diff(),
                    magic.getCost()));
            magic_counter++;
        }

        System.out.print("Choose the magic you want to cast: ");

        while (true) {
            String choice = scanner.nextLine();
            int choice_int;

            try {
                choice_int = Integer.valueOf(choice);
            } catch (NumberFormatException e) {
                System.out.print("-- (Error) Please type enemy number digit: ");
                continue;
            }

            if ((choice_int >= 1) && (choice_int <= magic_counter)) {
                return this.getMagics().get(choice_int - 1);
//                System.out.println(String.format("Report - %s attacked %s with %s damages.", this.name, enemies.get(choice_int).getName(), temp_dmg));
            } else {
                System.out.print("-- (Error) Please type a valid number: ");
            }
        }
    }

//    == Non-Static Method == Attack
    int attack(Person attacked_person) {
        return attacked_person.take_damage(this.generate_dmg());
    }

//    == Non-Static Method == Choose an Enemy from List
    Person choose_an_enemy(List<Person> enemies, String type) {
        switch (type) {
            case "attack":
                System.out.println("Choose the enemy to attack:");
                break;

            case "magic":
                System.out.println("Choose the enemy to cast magic:");
                break;
        }

        int enemy_counter = 0;

        for (int i = 0; i < enemies.size(); i++) {
            System.out.print(i + 1);
            if (enemies.get(i).getHp() > 0) {
                System.out.println(String.format(
                        ". %13s \t\t(HP %s/%s) (MP %s/%s)",
                        enemies.get(i).getName(),
                        enemies.get(i).getHp(),
                        enemies.get(i).getHp_max(),
                        enemies.get(i).getMp(),
                        enemies.get(i).getMp_max()
                ));
            } else {
                System.out.println(". DEAD");
            }
            enemy_counter++;
        }

        System.out.print("Choose: ");

        while (true) {
            String choice = scanner.nextLine();
            int choice_int;

            try {
                choice_int = Integer.valueOf(choice);
            } catch (NumberFormatException e) {
                System.out.println("-- (Error) Please type enemy number digit: ");
                continue;
            }

            if ((choice_int >= 1) && (choice_int <= enemy_counter)) {
                Person chosen_enemy = enemies.get(choice_int - 1);
                if (chosen_enemy.getHp() > 0)
                    return chosen_enemy;
                else
                    System.out.println("-- (Error) Please type a valid number: ");
//                System.out.println(String.format("Report - %s attacked %s with %s damages.", this.name, enemies.get(choice_int).getName(), temp_dmg));
            } else {
                System.out.println("-- (Error) Please type a valid number: ");
            }
        }

    }

//    == Non-Static Method == Print Status
    void status_print() {
        System.out.println(String.format("-Status- \t%13s \t\t(HP %s/%s) (MP %s/%s)", this.name, this.hp, this.hp_max, this.mp, this.mp_max));
    }
}
