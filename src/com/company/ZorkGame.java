package com.company;
import java.util.*;
public class ZorkGame {

    public static void main(String[] args) {
	// write your code here
        MazeFactory factory = new MazeFactory();
        MazeGame game = new MazeGame();
        game.createMaze(factory);

    }
}
class MazeFactory{
    public Maze makeMaze(){
        return new Maze();
    }
    public Wall makeWall(){
        return new Wall();
    }
    public Room makeRoom(){
        return new Room();
    }
    public Door makeDoor(Room r1, Room r2){
        return new Door(r1, r2);
    }
}
abstract class MapSite{
//    private String name;
//    private String description;
//
//    public MapSite(String newName, String newDescription) {
//        this.name = newName;
//        this.description = newDescription;
//    }
//    public String getName() {return  name;}
//    public void setName(String newName){ this.name = newName;}
//
//    public String getDescription() {return description; }
//    public void setDescription(String newDescription){this.description = newDescription; }
    abstract void enter();
}
enum Direction{
    North, South, East, West
}
//player
class Player{

}
//item
class Item{

}

class Door extends MapSite{
    private static int doorCount = 1;
    private int doorNum;
    private Room room1;
    private Room room2;
    Door(Room r1, Room r2){
        doorNum = doorCount++;
        System.out.println("creating a Door #" + doorNum + " between " + r1 + " and " + r2 );
        room1 = r1;
        room2 = r2;
    }
    public String toString(){
        return "Door #" + doorNum;
    }
    void enter(){}
}
class Wall extends MapSite{
    private int wallNum;
    private static int wallCount = 1;
    Wall(){
        wallNum = wallCount++;
        System.out.println("creating Wall #" + wallNum);
    }
    void enter(){}
}
class Room extends MapSite{
    private int roomNum;
    private static int roomCount = 1;
    private MapSite _north;
    private MapSite _south;
    private MapSite _east;
    private MapSite _west;
    Room(){
        roomNum = roomCount++;
        System.out.println("creating Room #" + roomNum);
    }

    public void setSide(Direction d, MapSite site){
        switch(d){
            case North:
                _north = site;
            case South:
                _south =site;
            case East:
                _east = site;
            case West:
                _west = site;
        }
        System.out.println("setting " + d.toString() + "side of" + this.toString() + "to" + site.toString());
    }
    public MapSite getSide(Direction d){
        MapSite result = null;
        switch(d){
            case North:
                result = _north;
            case South:
                result = _south;
            case East:
                result = _east;
            case West:
                result = _west;
        }
        return result;
    }
    public String toString(){
        return "room #" + roomNum;
    }
    void enter(){
        System.out.println("enter to Room " + roomNum);
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
    Room getRoom(String room){
        for(Room i : roomList){
            if(i.toString().equalsIgnoreCase(room)){
                return i;
            }
        }
        return roomList.get(0);
    }
}
//Maze
class MazeGame{
    public Maze createMaze(MazeFactory factory){
        Maze aMaze = factory.makeMaze();
        Room r1 = factory.makeRoom();
        Room r2 = factory.makeRoom();
        Room r3 = new Room1();
        Door theDoor = factory.makeDoor(r1, r2);
        aMaze.addRoom(r1);
        aMaze.addRoom(r2);
        aMaze.addRoom(r3);
        r1.setSide(Direction.North, factory.makeWall());
        r1.setSide(Direction.East, theDoor);
        r1.setSide(Direction.South, factory.makeWall());
        r1.setSide(Direction.West, factory.makeWall());
        r2.setSide(Direction.North, factory.makeWall());
        r2.setSide(Direction.East, factory.makeWall());
        r2.setSide(Direction.South, factory.makeWall());
        r2.setSide(Direction.West, theDoor);
        r2.setSide(Direction.East, factory.makeDoor(r2,r3));
        r3.setSide(Direction.North, factory.makeWall());
        r3.setSide(Direction.East, factory.makeWall());
        r3.setSide(Direction.South, factory.makeWall());
        r3.setSide(Direction.West, factory.makeDoor(r2, r3));
        return aMaze;
    }
}
class Room1 extends Room{
    Room1(){
        super();
    }
    public String toString(){
        return "room3" + super.toString();
    }
}
//parsing


//check command