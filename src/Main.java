import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int how_many_rooms, how_many_enemies, current_room;
        int current_battle = 0;
        Person player;

        game_main_loop:
        while (true) {

            player = new Person("The Hero", 1000, 200, 18, 3,
                    Magic.make_magic_list("fire-lightning-ice"));

            System.out.println(String.format("%s enter a dark dungeon...", player.getName()));

            how_many_rooms = (int) Math.floor((Math.random() * 5) + 10);

//            game_rooms_loop:
            for (int i = 0; i < how_many_rooms; i++) {

                current_room = i + 1;
                System.out.println(String.format("%s entered room %s.", player.getName(), current_room));

                how_many_enemies = (int) Math.floor((Math.random() * 2) + 1);

                List<Person> enemy_list = new ArrayList<>();

                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                integers.add(2);
                integers.add(3);
                for (int k = 0; k < how_many_enemies; k++) {
                    int random_index = (int) Math.floor(Math.random() * integers.size());
                    int random_enemy_code = integers.get(random_index);
                    integers.remove(random_index);

                    enemy_list.add(Person.make_an_enemy(random_enemy_code));
                }

                System.out.println(String.format("%s enemy(s) is in the room, you have to fight.", enemy_list.size()));

                boolean is_battle_continued = true;
                while (is_battle_continued) {
                    current_battle++;
                    System.out.println(String.format("--- Battle %s - Room %s ---", current_battle, current_room));
                    int result = battle(player, enemy_list, current_room, current_battle);

                    if (result == 0) {
                        is_battle_continued = false;
                    } else if (result == 2) {
                        System.out.println("---------------------------");
                        break game_main_loop;
                    }

                    System.out.println("---------------------------");
                }

            }

        }

    }

    private static int battle(Person hero, List<Person> list_of_enemies, int room_number, int battle_number) {

        Person selected_enemy = null;
        int dealed_damage;

        System.out.println("---    Characters Status:  ");
        hero.status_print();
        for (Person enemy : list_of_enemies) {
            enemy.status_print();
        }

//        Get player action choice
        int player_action = hero.choose_an_action();

        switch (player_action) {

            case 1:
                if (list_of_enemies.size() > 1) {
                    selected_enemy = hero.choose_an_enemy(list_of_enemies, "attack");
                } else if (list_of_enemies.size() == 1) {
                    selected_enemy = list_of_enemies.get(0);
                }
                if (selected_enemy != null) {
                    dealed_damage = hero.attack(selected_enemy);
                    System.out.println(String.format(
                            "\"%s\" attacked \"%s\" and deal %s damages.",
                            hero.getName(),
                            selected_enemy.getName(),
                            dealed_damage
                    ));
                }
                hero.setBlock_probability(5);
                break;

            case 2:
                Magic selected_magic = hero.choose_a_magic();

                if (list_of_enemies.size() > 1) {
                    selected_enemy = hero.choose_an_enemy(list_of_enemies, "magic");
                } else if (list_of_enemies.size() == 1) {
                    selected_enemy = list_of_enemies.get(0);
                }
                if (selected_enemy != null) {
                    dealed_damage = selected_enemy.take_damage(selected_magic.generate_dmg());
                    System.out.println(String.format(
                            "\"%s\" cast a %s toward \"%s\" and deal %s damages.",
                            hero.getName(),
                            selected_magic.getName(),
                            selected_enemy.getName(),
                            dealed_damage
                    ));
                }
                hero.setBlock_probability(5);
                break;

            case 3:
                hero.setBlock_probability(80);
                System.out.println(String.format(
                        "\"%s\" block for next turn. (Additional Block Chance upto 80 percent)",
                        hero.getName()
                ));
                break;
        }

//        Check if all enemies are dead?
        boolean all_enemies_dead = true;
        for (Person enemy : list_of_enemies) {
            if (enemy.getHp() > 0) {
                all_enemies_dead = false;
            }
        }
        if (all_enemies_dead) {
            System.out.println(String.format(
                    "All %s Enemy(s) Killed by \"%s\" in Room %s (Battle: %s).\n\"%s\" go through the door to next room.",
                    list_of_enemies.size(),
                    hero.getName(),
                    room_number,
                    battle_number,
                    hero.getName()
            ));
            return 0;
        }

//        Every enemy attack turn by turn
        for (Person enemy : list_of_enemies) {

//            Get enemy action choice
            int cpu_action = (int) Math.floor((Math.random() * 3) + 1);

            switch (cpu_action) {

                case 1:
                    dealed_damage = enemy.attack(hero);
                    System.out.println(String.format(
                            "\"%s\" attacked \"%s\" and deal %s damages.",
                            enemy.getName(),
                            hero.getName(),
                            dealed_damage
                    ));
                    enemy.setBlock_probability(5);
                    break;

                case 2:
                    int random_magic_index = (int) Math.floor(Math.random() * enemy.getMagics().size());
                    Magic selected_magic = enemy.getMagics().get(random_magic_index);

                    dealed_damage = hero.take_damage(selected_magic.generate_dmg());
                    System.out.println(String.format(
                            "\"%s\" cast a %s toward \"%s\" and deal %s damages.",
                            enemy.getName(),
                            selected_magic.getName(),
                            hero.getName(),
                            dealed_damage
                    ));
                    enemy.setBlock_probability(5);
                    break;

                case 3:
                    enemy.setBlock_probability(80);
                    System.out.println(String.format(
                            "\"%s\" block for next turn. (Additional Block Chance upto 80 percent)",
                            enemy.getName()
                    ));
                    break;

            }

//            Check if player is dead?
            if (hero.getHp() <= 0) {
                System.out.println(String.format(
                        "\"%s\" Killed in Room %s (Last Battle: %s).",
                        hero.getName(),
                        room_number,
                        battle_number
                ));
                return 2;
            }

        }

        return 1;

    }

}
