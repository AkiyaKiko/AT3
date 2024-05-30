/**
 * KXO151 Assignment 3, 2024
 *
 * Mystery Mansion -- Library class
 *
 * MysteryMansionBot.java
 *
 * Notes: Students should NOT change this program
 * 
 * Contributors: Chengwei Yan 
 *               Student ID: 694659
 *               Email: cyan4@utas.edu.au
 *               Github: https://github.com/ChenxiMiku
 *               Wentao Su 
 *               Student ID: 694641
 *               Email: wentaos@utas.edu.au
 *               Github: https://github.com/AkiyaKiko
 * 
 * Statement of Purpose: This program is designed to play the Mystery Mansion Game.
 */


import java.util.Random;


public class MysteryMansionBot 
{
    // final instance variables
    public final int IMPOSSIBLE = -1;	// operation unsuccessful because it was not allowed
    public final int SUCCESS = 0;		// operation successful
    public final int EATEN = 1;			// operation unsuccessful because player found by ghost
    public final int FELL = 2;			// operation unsuccessful because player fell in pit
    public final int FAILURE = 3;		// operation unsuccessful for reason other than above two reasons
    
    private final int NUM_ROOM = 8;					// number of ROOMS
    private final int[] RIGHT_ROOM = {3,0,1,2,5,6,7,4};	// ROOMS to right
    private final int[] LEFT_ROOM = {1,2,3,0,7,4,5,6};	// ROOMS to left
    private final int[] MID_ROOM = {4,5,6,7,0,1,2,3};	// ROOMS to front

    //non-final instance variables
    private int currRoom;		// current location of player
    private int ghostPos;		// room where the ghost lives
    private int pitPos;			// room where the pit is found
    private Random generator; 	// to use for random placement in rooms
    private boolean tracing;	// switch for tracing messages
    
    
    /** 
     *  creates a bot for a mystery mansion game
     */
    public MysteryMansionBot() 
    {
        generator = new Random();
        tracing = true;				// assume program is being debugged
        generator.setSeed(101);		// seed RNG for tracing purposes
    }
    
    
    /** 
     *  provide the number of rooms in the room system
     *  @return indicates how many rooms in the system
     */
    public int getNumRooms()
    {
        return NUM_ROOM;
    }
    
    
    /** 
     *  randomly determines unique locations of start (currentCave), pit, ghost
     *  @return indicates the starting location (room)
     */
    public int newGame()
    {
        int pos;
        
        // determine player's position
        pos = generator.nextInt(NUM_ROOM);
        currRoom = pos;
        trace("player starts at " + currRoom);
        
        // determine ghost' position
        pos = generator.nextInt(NUM_ROOM);
        while (pos == currRoom)
        {
            pos = generator.nextInt(NUM_ROOM);
        }
        ghostPos = pos; 
        trace("Ghost is at " + ghostPos);
        
        // determine position of pit
        pos = generator.nextInt(NUM_ROOM);
        while ((pos == currRoom) || (pos == ghostPos))
        {
            pos = generator.nextInt(NUM_ROOM);
        }
        pitPos = pos;
        trace("pit is at " + pitPos);
        
        return currRoom;
    }
    
    
    /** 
     *  determine if Ghost is in adjacent room
     *  @return whether Ghost location is in a room connected to current
     */
    public boolean ghostNear()
    {
        boolean isClose = false;
        
        if ((RIGHT_ROOM[currRoom ] == ghostPos) ||
            (MID_ROOM[currRoom ] == ghostPos) ||
            (LEFT_ROOM[currRoom ] == ghostPos) )
        {
            isClose = true;
            trace("Ghost is close ");
        }
        else
        {
        	trace("Ghost is not close");
        }
        
        return isClose;
    }
    
    
    /** 
     *  determine if pit is in adjacent room
     *  @return whether pit location is in a room connected to current
     */
    public boolean pitNear()
    {
        boolean isClose = false;
        
        if ((RIGHT_ROOM[currRoom ] == pitPos) ||
            (MID_ROOM[currRoom ] == pitPos) ||
            (LEFT_ROOM[currRoom ] == pitPos) )
        {
            isClose = true;
            trace("pit is close");
        }
        else
        {
        	trace("pit is not close");
        }
        
        return isClose;
    }
    
    
    /** 
     *  try to move the player to another room
     *  @param into indicates the room to try to move into
     *  @return status of movement
     *      SUCCESS: move was successful (current position changed)
     *      IMPOSSIBLE: move was impossible (current position not changed)
     *      EATEN: moved and was eaten
     *      FELL: moved and fell in pit
     */
    public int tryWalk(int into)
    {
        int result;
        
        // check direction
        if ((RIGHT_ROOM[currRoom ] == into) ||
            (MID_ROOM[currRoom ] == into) ||
            (LEFT_ROOM[currRoom ] == into))
        {
            trace("move into Room " + into );
            currRoom = into;
            if (currRoom == ghostPos)
            {
                result = EATEN; 
            }
            else
            {
                if (currRoom == pitPos)
                {
                    result = FELL; 
                }
                else
                {
                    result = SUCCESS; 
                }
            }
        }
        else
        {
            result = IMPOSSIBLE;
        }
        
        trace("result of attempt to move: " + result);
        
        return result;
    }
    
    
    /** 
     *  try to shoot into another room
     *  @param into indicates the room to try to shoot into
     *  @return status of shooting
     *      SUCCESS: shot was successful (ghost killed)
     *      IMPOSSIBLE: shot was impossible (no arrow used)
     *      FAILURE: shot was unsuccessful (ghost not present)
     */
    public int tryShoot(int into)
    {
        int result;
        
        if((RIGHT_ROOM[currRoom ] == into) ||
           (MID_ROOM[currRoom ] == into) ||
           (LEFT_ROOM[currRoom ] == into))
        {
            trace("shoot  into Room " + into );
            trace("Ghost is at " + ghostPos);
            
            if (into == ghostPos)
            {
                result = SUCCESS; 
            }
            else
            {
                result = FAILURE; 
            }
        }
        else
        {
            result = IMPOSSIBLE;
        }
        
        trace("result of attempt to shoot: " + result);
        
        return result;
    }


    /** 
     *  provide the number of the current room
     *  @return which room number player is within
     */
    public int getCurrent()
    {
    	trace("in Room number " + currRoom);
        return currRoom;
    }
    
    
    /** 
     *  determine number of adjacent room given its direction
     *  @param char indicates the direction required (l - left, r - right, a - ahead)   *  @return status of movement
     *  @return number of room in that direction or IMPOSSIBLE if invalid parameter
     */
    public int nextRoom(char direction)
    {
        final char LEFT = 'l';	// left
        final char AHEAD = 'a';	// ahead
        final char RIGHT = 'r';	// right
        
        int nextIs;
        
        switch (direction)
        {
            case LEFT:  nextIs = LEFT_ROOM[currRoom];
                        break;
            case AHEAD: nextIs = MID_ROOM[currRoom];
                        break;
            case RIGHT: nextIs = RIGHT_ROOM[currRoom];
                        break;
            default:    nextIs = IMPOSSIBLE;
        }
        trace("next Room on " + direction + " is " + nextIs);
        
        return nextIs;
    }  
        
        
    /** 
     *  turn tracing messages on or off (if off it is assumed that debugging is not occurring and so a new (unseeded) RNG is provided
     *  @param onOff indicates the required state of messages (true on, false off)
     */
    public void setTracing(boolean onOff)
    {
    	if (! onOff)	// not tracing so get an unseeded RNG
    	{
    		generator=new Random();
    	}
    	
        tracing = onOff;
    }
    
    
    /** 
     *  displays tracing messages
     *  @param	message	the message to be displayed if instance variable tracing is true
     */
    public void trace(String message)
    {
        if (tracing)
        {
            System.out.println("GhostBot: " + message);
        }
    }
}

