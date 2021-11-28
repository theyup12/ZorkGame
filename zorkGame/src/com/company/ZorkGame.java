package com.company;

import java.util.*;

class Parser {
    private String input;

    Parser(String input){
        this.input = input.toLowerCase().trim();
    }

    public String[] parsing(){

        String[] userInput = this.input.split(" ");
        if(userInput.length > 2){
            return new String[0];
        } else if (userInput.length == 2) {
            ArrayList<String> actions = new ArrayList<>(Arrays.asList("go", "walk", "open", "view"));
            ArrayList<String> objects = new ArrayList<>(Arrays.asList("north", "south", "west", "east", "bag",
                    "letter", "key", "keys", "wallet", "money", "Americano", "rock", "water", "cake", "basketball"));

            if(!actions.contains(userInput[0])){
                return new String[0];
            }
            if(!objects.contains(userInput[1])){
                return new String[0];
            }
        }else if(userInput.length == 1){
            ArrayList<String> actions = new ArrayList<>(Arrays.asList("search", "help"));
            if(!actions.contains(userInput[0])){
                return new String[0];
            }
        }
        return userInput;
    }
}

class MapSite{
    boolean isRoom;
    boolean isDoor;
    boolean isWall;
    String name;
    public String[] enter(){
        String[] ret = new String[2];
        ret[1] = name;
        if(isRoom){
            ret[0] = "room";
        }else if(isDoor){
            ret[0] = "door";
        }else if(isWall){
            ret[0] = "wall";
        }
        return ret;
    }
}

class Room extends MapSite{
    private MapSite East;
    private MapSite West;
    private MapSite North;
    private MapSite South;
    private String description;
    HashMap<String, Integer> items;
    public Room(){
        this.isRoom = true;
        items = new HashMap<>();
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMap(Direction d, MapSite neighbor){
        switch (d) {
            case north -> North = neighbor;
            case south -> South = neighbor;
            case east -> East = neighbor;
            case west -> West = neighbor;
        }
    }
    public MapSite next(Direction d){
        return switch (d) {
            case east -> East;
            case west -> West;
            case north -> North;
            case south -> South;
        };
    }

    public void addItems(String key, int val){
        items.put(key, val);
    }
    public Player viewAndGetItems(Player player){
        String key = "";
        int value = 0;
        if(items.isEmpty()){
            System.out.println("You did not find anything here.");

        }else{
            for(String k : items.keySet()){
                key = k;
                value = items.get(k);
                break;
            }
            Scanner s = new Scanner(System.in);
            if(key.equals("money")){
                System.out.println("You've found money: $" + value + ".");
                System.out.println("Would you like to put it into bag?(yes/no)");
                System.out.print("> ");
                String input = s.nextLine().trim();
                while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")){
                    System.out.println("Invalid input.");
                    System.out.println("Would you like to put it into bag?(yes/no)");
                    System.out.print("> ");
                    input = s.nextLine().trim();
                }
                if(input.equals("yes")){
                    player.grab("wallet", value);
                    items.remove(key);
                    System.out.println("$" + value +" has been add to your wallet.");
                }
            }else if(items.get(key) == 0){
                System.out.println("You've found a "+ key +".");
                System.out.println("Would you like to put it into bag?(yes/no)");
                System.out.print("> ");
                String input = s.nextLine().trim();
                while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")){
                    System.out.println("Invalid input.");
                    System.out.println("Would you like to put it into bag?(yes/no)");
                    System.out.print("> ");
                    input = s.nextLine().trim();
                }
                if(input.equals("yes")) {
                    player.grab(key, 1);
                    items.remove(key);
                    System.out.println("One " + key + " has been put it in your bag.");
                }

            }else {
                System.out.println("would you like to buy " + key + " for $" + value + "?(yes/no)");
                System.out.print("> ");
                String input = s.nextLine().trim();
                while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
                    System.out.println("Invalid input.");
                    System.out.println("would you like to buy " + key + " for $" + value + "?(yes/no)");
                    System.out.print("> ");
                    input = s.nextLine().trim();
                }
                if (input.equals("yes")) {
                    if(player.useMoney(value)){
                        player.grab(key, 1);
                        items.remove(key);
                    }
                }
            }
        }
        return player;
    }

}

class Wall extends MapSite{
    String description;
    public Wall(){
        isWall = true;
        name = "wall";
        description = "This is a wall!";
    }
}


class Door extends MapSite{
    private Room room1;
    private Room room2;
    private boolean open = true;
    public Door(){
        this.isDoor = true;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setRooms(Room room1, Room room2) {
        this.room1 = room1;
        this.room2 = room2;
    }
    public Room next(String currentRoom, Player player){
        if(open){
            if(room1.getName().equals(currentRoom)){
                return room2;
            }else{
                return room1;
            }
        }else{
            System.out.println("The door is locked!");

            System.out.println("Would you like to use a key to unlock this door?(yes/no)");
            Scanner scanner = new Scanner(System.in);
            String input;

            input = scanner.nextLine().trim();
            while(!input.toLowerCase().equals("yes") && !input.toLowerCase().equals("no")){
                System.out.println("Invalid input.");
                System.out.println("Would you like to use a key to unlock this door?(yes/no)");
                input = scanner.nextLine().trim();
            }
            if(input.equals("yes")){
                this.open = player.useKey();
                if(open){
                    if(room1.getName().equals(currentRoom)){
                        return room2;
                    }else{
                        return room1;
                    }
                }else{
                    if(room1.getName().equals(currentRoom)){
                        return room1;
                    }else{
                        return room2;
                    }
                }
            }else{
                if(room1.getName().equals(currentRoom)){
                    return room1;
                }else{
                    return room2;
                }
            }
        }

    }
    public void locked(){
        this.open = false;
    }
}

enum Direction{
    north, south, east, west;
}


class MazeFactory{
    private HashMap<String, Room> roomBook;
    private Wall wall = new Wall();
    private HashMap<String, Door> doorBook;
    private MapSite maze;

    public MazeFactory(){
        roomBook = new HashMap<>();
        doorBook = new HashMap<>();
    }
    public HashMap<String, Room> getRoomBook() {
        return roomBook;
    }

    public Wall getWall() {
        return wall;
    }

    public HashMap<String, Door> getDoorBook() {
        return doorBook;
    }

    public Room addRoom(String name){
        Room room = new Room();
        room.setName(name);
        roomBook.put(name, room);
        return room;
    };

    public Room addRoom(String name, String description){
        Room room = new Room();
        room.setName(name);
        room.setDescription(description);
        roomBook.put(name, room);
        return room;
    }

    public Room addRoom(String name, String description, MapSite north, MapSite south, MapSite west, MapSite east){
        Room room = new Room();

        room.setName(name);
        room.setDescription(description);
        room.setMap(Direction.north, north);
        room.setMap(Direction.south, south);
        room.setMap(Direction.west, west);
        room.setMap(Direction.east, east);
        roomBook.put(name, room);
        return room;
    }

    public Wall addWall(){
        return wall;
    }

    public Door addDoor(String name){
        Door door = new Door();
        door.setName(name);
        doorBook.put(name, door);
        return door;
    }
    public Door addLockDoor(String name){
        Door door = new Door();
        door.setName(name);
        door.locked();
        doorBook.put(name, door);
        return door;
    }
    public Door addDoor(String name, Room room1, Room room2){
        Door door = new Door();
        door.setName(name);
        door.setRooms(room1, room2);
        doorBook.put(name, door);
        return door;
    }
    public Door addLockDoor(String name, Room room1, Room room2){
        Door door = new Door();
        door.locked();
        door.setName(name);
        door.setRooms(room1, room2);
        doorBook.put(name, door);
        return door;
    }
    public MapSite Start(){
        return maze;
    }
    public MapSite createMaze(){ // use only once

        Room start = addRoom("start","This is the starting point");
        Room mailbox = addRoom("mailbox", "This is a mailbox, something is inside of it.");
        mailbox.addItems("letter", 0);
        mailbox.addItems("key", 0);

        Door door1 = addDoor("startToMailbox",start, mailbox);
        Wall wall = addWall();

        Room west = addRoom("west", "This is the West Courtyard of the house.");
        Door door2 = addLockDoor("mailboxToWest", mailbox, west);

        Room north = addRoom("north", "This is the North Courtyard of the house.");
        Door door3 = addDoor("westToNorth", west, north);
        north.addItems("key", 0);

        Room south = addRoom("south", "This is the South Courtyard of the house.");
        Door door4 = addDoor("westToSouth", west, south);
        south.addItems("key", 0);

        Room east = addRoom("east", "This is the East Courtyard of the house.");
        Door door5 = addLockDoor("northToEast", north, east);

        Room livingRoom = addRoom("livingroom", "You are inside of house. This is the Living Room.");
        Door door6 = addLockDoor("northToLivingroom", north, livingRoom);

        Door door7 = addDoor("southToEast", south, east);

        Room bedroom = addRoom("bedroom", "This is your bedroom.");
        Door door8 = addDoor("livingroomToBedroom", livingRoom, bedroom);
        bedroom.addItems("money", 10);

        Room attic = addRoom("attic", "This is the Attic.");
        Door door9 = addDoor("livingroomToAttic", livingRoom, attic);
        attic.addItems("key", 0);

        Room kitchen = addRoom("kitchen", "This is the kitchen, you can look through the door.");
        Door door10 = addDoor("kitchenToLivingroom", kitchen, livingRoom);

        Door door11 = addDoor("eastToKitchen", east, kitchen);

        Room garage = addRoom("garage", "This is the garage.");
        Door door12 = addLockDoor("eastToGarage", east, garage);

        Room bridge = addRoom("bridge", "this is the bridge and heading to the school.");
        Door door13 = addDoor("garageToBridge", garage, bridge);
        bridge.addItems("rock", 0);
        bridge.addItems("money",5);

        Room department = addRoom("department", "This is the CS department in the school.");
        Door door14 = addDoor("bridgeToDepartment", bridge, department);

        Room office = addRoom("office", "This is the office, ask for a day off");
        Door door15 = addDoor("departmentToOffice", department, office);

        Room parkingLot = addRoom("parkinglot", "This is the parking lot in the school");
        Door door16 = addDoor("departmentToParkinglot", department,parkingLot);

        Room starbucks = addRoom("starbucks", "This is the starbucks, and there are a lot of people waiting");
        Door door17 = addDoor("parkinglotToStarbucks", parkingLot, starbucks);
        starbucks.addItems("Americano",5);

        Room park = addRoom("park", "This is the park next to the school");
        Door door18 = addDoor("parkinglotToPark", parkingLot, park);
        park.addItems("water", 2);
        park.addItems("basketball",0);
        park.addItems("cake", 8);

        Room esportCafe = addRoom("esportcafe", "welcome to the esports cafe, this is a place where you can find happiness");
        Door door19 = addDoor("parkToEsportcafe", esportCafe, park);

        // add more room/door/wall based on game map
        start.setMap(Direction.east, door1);
        start.setMap(Direction.west,wall);
        start.setMap(Direction.north, wall);
        start.setMap(Direction.south, wall);

        mailbox.setMap(Direction.north, wall);
        mailbox.setMap(Direction.south, wall);
        mailbox.setMap(Direction.east, door2);
        mailbox.setMap(Direction.west, door1);

        west.setMap(Direction.west, door2);
        west.setMap(Direction.east, wall);
        west.setMap(Direction.north, door3);
        west.setMap(Direction.south, door4);

        south.setMap(Direction.west,door4);
        south.setMap(Direction.east,door7);
        south.setMap(Direction.north,wall);
        south.setMap(Direction.south,wall);

        north.setMap(Direction.west, door3);
        north.setMap(Direction.east, door5);
        north.setMap(Direction.north, wall);
        north.setMap(Direction.south, door6);

        east.setMap(Direction.west, door11);
        east.setMap(Direction.east, door12);
        east.setMap(Direction.north, door5);
        east.setMap(Direction.south, door7);

        livingRoom.setMap(Direction.west, door8);
        livingRoom.setMap(Direction.east, door10);
        livingRoom.setMap(Direction.north, door6);
        livingRoom.setMap(Direction.south, door9);

        bedroom.setMap(Direction.west, wall);
        bedroom.setMap(Direction.east, door8);
        bedroom.setMap(Direction.north, wall);
        bedroom.setMap(Direction.south, wall);

        attic.setMap(Direction.west, wall);
        attic.setMap(Direction.east, wall);
        attic.setMap(Direction.north, door9);
        attic.setMap(Direction.south, wall);

        kitchen.setMap(Direction.west, door10);
        kitchen.setMap(Direction.east, door11);
        kitchen.setMap(Direction.north, wall);
        kitchen.setMap(Direction.south, wall);

        garage.setMap(Direction.west, door12);
        garage.setMap(Direction.east, wall);
        garage.setMap(Direction.north, door13);
        garage.setMap(Direction.south, wall);

        bridge.setMap(Direction.west, wall);
        bridge.setMap(Direction.east, wall);
        bridge.setMap(Direction.north, door14);
        bridge.setMap(Direction.south, door13);

        department.setMap(Direction.west, door16);
        department.setMap(Direction.east, door15);
        department.setMap(Direction.north, wall);
        department.setMap(Direction.south, door14);

        office.setMap(Direction.west, door15);
        office.setMap(Direction.east, wall);
        office.setMap(Direction.north, wall);
        office.setMap(Direction.south, wall);

        parkingLot.setMap(Direction.west, door17);
        parkingLot.setMap(Direction.east, door16);
        parkingLot.setMap(Direction.north, door18);
        parkingLot.setMap(Direction.south, wall);

        starbucks.setMap(Direction.west, wall);
        starbucks.setMap(Direction.east, door17);
        starbucks.setMap(Direction.north, wall);
        starbucks.setMap(Direction.south, wall);

        park.setMap(Direction.west, door19);
        park.setMap(Direction.east, wall);
        park.setMap(Direction.north, wall);
        park.setMap(Direction.south, door18);

        esportCafe.setMap(Direction.west, wall);
        esportCafe.setMap(Direction.east, door19);
        esportCafe.setMap(Direction.north, wall);
        esportCafe.setMap(Direction.south, wall);

        maze = start;
        return maze;
    }
}

class Letter{
    public Letter(String name){
        System.out.println("Dear "+ name +",\n" +
                "\n" +
                "One more year has gone away from us, and we all are eagerly waiting for Christmas\n" +
                " and New year. Please let me take this opportunity to wish you and your family a \n" +
                "Blessed Christmas Season. Also, may the coming year bring you all the success and \n" +
                "happiness and let good luck follow you all the time.\n" +
                "\n" +
                "I consider it a blessing to be able to get a chance to work with you. I truly \n" +
                "cherish the friendship we share in our workplace. My family and I sincerely wish \n" +
                "that this year may bring love, luck, and prosperity to you and your family. We \n" +
                "pray that you will get good health and wealth and let this year be an amazing one \n" +
                "for you. Once again I wish you a Merry Christmas and a Happy New year!\n"+
                "\n"+
                "Best Regards,\n" +
                "Andy Cao");
    }
}

class Player{
    String name;
    HashMap<String, Integer> items = new HashMap<>();
    public Player(String name){
        this.name = name;
        items.put("wallet", 5);
        items.put("key", 0);
    }
    public boolean useKey(){
        if(items.get("key") > 0){
            System.out.println("You have used a key to unlocked a door.");
            items.put("key", items.get("key") - 1);
            return true;
        }else{
            System.out.println("You do not have any key left.");
            return false;
        }
    }
    public boolean useMoney(int cost){

        if(items.get("wallet") > cost){
            int money = items.get("wallet") - cost;
            items.put("wallet", money);
            System.out.println("You have $ " + money + " in your wallet");
            return true;
        }else{
            System.out.println("You do not have enough money");
            System.out.println("You have only $" + items.get("wallet") + " in your wallet");
            return false;
        }
    }
    public int checkMoney(){
        return items.get("wallet");
    }
    public void viewItems(String itemName){
        if(items.containsKey(itemName)){
            if (itemName.equals("letter")){
                Letter letter = new Letter(name);
            }else if(itemName.equals("money") || itemName.equals("wallet")){
                System.out.println("You have $" + items.get("wallet") + " in your wallet.");
            }else if (itemName.equals("key")|| itemName.equals("keys")){
                System.out.println("You have " + items.get("key") + " key(s) in your bag");
            }
        }else if (itemName.equals("bag")) {
            System.out.println("You have the following items in your bag: ");
            if (items.isEmpty()) {
                System.out.print("no item in you bag.");
            } else {
                for (String key : items.keySet()) {

                    if(key.equals("key")){
                        if (items.get("key") == 0){
                            continue;
                        }
                    }
                    System.out.print("\t");
                    System.out.println(key);
                }
                System.out.println("You may enter command 'view {item name}' for more detail. ");
            }
        }
    }
    public void grab(String key, int value){
        if(items.containsKey(key)){
            items.put(key, items.get(key)+ value);
        }else{
            items.put(key, value);
        }
    }

}

class Info{
    public Info(){
        System.out.println("""
                        +---------------------------------------------------------------------------+
                        Welcome to the Zork Game!
                        Here are some useful commands to lead you to the success!
                        help                             -  get the information for using commands.
                        go/walk + east/west/north/south  -  go to the next(previous) room.
                        search                           -  search item at current room.
                        * Notice you may only search one item at a time, however there might be more
                            than one item in a room.
                        view bag                         -  view all the items in you bag.
                        view + {item name}               -  view a item in your bag.
                        q/quit                           -  quit this game.
                        +---------------------------------------------------------------------------+
                        """
        );
    }

}
class Game{

    public Room runCommand(String[] instructions, MazeFactory f, Room current, Player player){
        if(instructions.length == 2){
            if(instructions[0].equals("go") || instructions[0].equals("walk")){
                Direction d = Direction.valueOf(instructions[1]);
                String[] checkNext = current.next(d).enter();
                if(checkNext[0].equals("door")){
                    Door nextDoor = f.getDoorBook().get(checkNext[1]);
                    return nextDoor.next(current.getName(),player);
                }else if(checkNext[0].equals("wall")) {
                    System.out.println("This is a wall!");
                    return current;
                }
            }else if(instructions[0].equals("view")){
                player.viewItems(instructions[1]);
            }
        }else if (instructions.length == 1){
            if (instructions[0].equals("search")){
                player = current.viewAndGetItems(player);
            }

        }

        return current;
    }

    public void runGame(){
        String input;
        MazeFactory f = new MazeFactory();
        MapSite map = f.createMaze();
        String[] current = map.enter();
        Room currentRoom = f.getRoomBook().get(current[1]);
        System.out.println("Please enter your name: ");
        System.out.print("> ");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine().trim();
        Player player = new Player(name);
        System.out.println("GAME START!!!");
        Info info = new Info();
        do{
            System.out.println(currentRoom.getDescription());
            System.out.print("> ");
            input = s.nextLine();
            if(input.isEmpty()){
                System.out.println("Invalid instruction! Please try it again.1");
                continue;
            }
            Parser parser = new Parser(input);
            String[] tokens = parser.parsing();
            if(tokens.length == 0){
                System.out.println("Invalid instruction! Please try it again.2");
                continue;
            }

            currentRoom = runCommand(tokens, f, currentRoom, player);
            //check winning condition
            if(current[1].equals("esportcafe")){
                if (player.checkMoney() >= 3){
                    System.out.println("This is the destination! You win!");
                    System.exit(0);
                }else{
                    System.out.println("You do not have enough money. " + "\n"+
                            "Please go home to get money and come back later.");
                }
            }
        }while(!input.equals("q") && !input.equals("quit"));

    }
}
public class ZorkGame{
    public static void main(String[] args) {
        Game game = new Game();
        game.runGame();
    }
}
//    Please enter your name:
//        > player1
//        GAME START!!!
//        +---------------------------------------------------------------------------+
//        Welcome to the Zork Game!
//        Here are some useful commands to lead you to the success!
//        help                             -  get the information for using commands.
//        go/walk + east/west/north/south  -  go to the next(previous) room.
//        search                           -  search item at current room.
//        * Notice you may only search one item at a time, however there might be more
//        than one item in a room.
//        view bag                         -  view all the items in you bag.
//        view + {item name}               -  view a item in your bag.
//        q/quit                           -  quit this game.
//        +---------------------------------------------------------------------------+
//
//        This is the starting point
//        > go east
//        This is a mailbox, something is inside of it.
//        > search
//        You've found a letter.
//        Would you like to put it into bag?(yes/no)
//        > yes
//        One letter has been put it in your bag.
//        This is a mailbox, something is inside of it.
//        >