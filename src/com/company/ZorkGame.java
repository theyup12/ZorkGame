package com.company;

import java.util.*;

public class ZorkGame {
    public static void main(String[] args) {
        // write your code here
        MazeGame game = new MazeGame();
        game.createMaze();

        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        System.out.println(Arrays.toString(input.split(" ")));
    }
}
//String: go north, walk north, pick key, take key, drink potion, read
class Game{

}
class Parser {
    private String input;

    Parser(String input){
        this.input = input.toLowerCase().trim();
    }
    public String[] parsing(){

        String[] userInput = this.input.split(" ");
        if(userInput.length > 2){
            return null;
        } else if (userInput.length == 2) {
            ArrayList<String> actions = new ArrayList<>(Arrays.asList("go", "walk", "take", "see", "talk"));
            ArrayList<String> objects = new ArrayList<>(Arrays.asList("north", "south", "west", "east", "box", "letter", "key","wallet", "money", "bush"));

            if(!actions.contains(userInput[0])){
                return null;
            }
            if(!objects.contains(userInput[1])){
                return null;
            }
        }else if(userInput.length == 1){
            if(!userInput[0].equals("pay")){
                return null;
            }
        }
        return userInput;
    }
}

class Room {
    private String roomName;
    private String description;
    private ArrayList<String> items;
    private HashMap<String, Room>map = new HashMap<>();
    //---------------------------------------
    public void setMap(String direction, Room nextRoom) {

        this.map.put(direction, nextRoom);
    }

    //---------------------------------------
    public void showItem() {
        //ToDo
        System.out.println("item");
    }

    public String Pickup(String command) {
        if (items.contains(command)) {
            int index = items.indexOf(command);
            return items.remove(index);
        }else{
            return "";
        }
    }

    //---------------------------------------
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    //---------------------------------------
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //---------------------------------------
    //ToDo direction
}
class Player{
    private ArrayList<String> inventory;
    private int health = 100;

    public void setInventory(ArrayList<String> inventory) {
        this.inventory = inventory;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
class Wall{
    private int wallNum;
    private static int _wallCount = 1;
    Wall(){
        wallNum = _wallCount++;
        System.out.println("this is the wall boy");
    }
}
class Door {
    private Room room1;
    private Room room2;
    private String direction;
    private boolean key = false;
    public Room openDoor(){
        if(key){
            if(this.direction.equals("east")){
                return this.room2;
            }else{
                return this.room1;
            }
        }else{
            if(this.direction.equals("east")){
                return this.room1;
            }else{
                return this.room2;
            }
        }
    }
    public void setKey(boolean key){
        this.key = key;
    }
    Door(String direction){
        this.direction = direction;
    }

}
class Maze{
    private List<Room> roomList = new ArrayList<>();
    Maze(){
        System.out.println("creating a Maze");
    }
    void addRoom(Room r){
        if(!roomList.contains(r)){
            roomList.add(r);
        }
    }
}
class MazeGame{
    public Maze createMaze(){
        Maze aMaze = new Maze();
        Room r1 = new Room();
        r1.setRoomName("living room");
        r1.setDescription("Inside the Living room");
        Room r2 = new Room();
        r2.setRoomName("bedroom");
        r2.setDescription("Inside the bedroom");
        r1.setMap("east", r2);
        r2.setMap("west", r1);
        return aMaze;
    }
}