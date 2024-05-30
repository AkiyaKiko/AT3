/**
 * KXO151 Assignment 3 (2024) Sample Solution
 *
 * Mystery Mansion
 *
 * MysteryMansionGame.java
 * 
 * Contributors: Chengwei Yan 
 *               Student ID: 694659
 *               Email: cyan4@utas.edu.au
 *               Github: https://github.com/ChenxiMiku
 *               Wentao Su 
 *               Student ID: 694641
 *               Email: wentaos@utas.edu.au
 *               Github: https://github.com/AkiyaKiko
 * Github repository: https://github.com/AkiyaKiko/AT3
 * 
 * Statement of Purpose: This program is designed to play the Mystery Mansion Game.
 */
 
 
import java.util.Scanner;

public class MysteryMansionGame {
    private final int MAXARROWS = 3;
    public int MAX_ARROWS;
    private final int TOTAL = 0;
    private final int WON = 1;
    private final int EATEN = 2;
    private final int FELL = 3;
    private final int QUIT = 4;
    private final String[] RESPONSES = {"Ow, that hurt.  You just walked into a wall...",
            "Walk successful.",
            "You're dead.  The Ghost gotcha!",
            "You're dead.  You just fell in the pit.",
            "Arrow missed.  I guess the ghost wasn't in there...",
            "It's a direct hit.  You've killed the Ghost!",
            "What a waste.  The arrow just bounced off the wall...",
            "You can't shoot -- you have no arrows left!"};

    private boolean tracing;
    private MysteryMansionBot bot;
    private Scanner sc;
    private boolean gameOver;
    private int numArrows;
    private int response;
    private int[] gamesPlayed = {0, 0, 0, 0, 0};
    private String[] gamesDesc = {"You played a total of ",
            "You won ",
            "You lost (by being eaten) ",
            "You lost (by falling in the pit) ",
            "You lost (due to quitting) "};
    private int difficultyLevel;

    public MysteryMansionGame() {
        bot = new MysteryMansionBot();
        sc = new Scanner(System.in);


        play();
    }

    // Set difficulty level
    public void setDifficultyLevel() {
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.print("Enter your choice: ");
        difficultyLevel = sc.nextInt();
        System.out.println();

        // Set the number of arrows based on the difficulty level
        switch (difficultyLevel) {
            case 1:
                MAX_ARROWS = MAXARROWS + 1; // Provide more arrows for easy mode
                break;
            case 2:
                MAX_ARROWS = MAXARROWS;
                break;
            case 3:
                MAX_ARROWS = MAXARROWS - 1; // Reduce arrows for hard mode
                break;
            default:
                MAX_ARROWS = MAXARROWS;
        }
    
    }

    //
    public void play() {
        tracing = false;
        bot.setTracing(false); // Turn off tracing
        setDifficultyLevel(); // Set difficulty level
        explain(); // Explain the game

        System.out.print("Would you like to play Mystery Mansion Game? ");
        String play = sc.next();
        numArrows = MAX_ARROWS; // Store the number of arrows

        while(play.equalsIgnoreCase("y")){
            gameOver = false;
            numArrows = MAX_ARROWS;
            gamesPlayed[TOTAL]++;
            bot.newGame();
            while (!gameOver) {
                playOne();
            }
            System.out.print("Would you like to play Mystery Mansion Game? ");
            play = sc.next();
        }

        // Print the number of games played
        System.out.println(gamesDesc[TOTAL] + gamesPlayed[TOTAL] + " games.");
        System.out.println(gamesDesc[WON] + gamesPlayed[WON] + " games.");
        System.out.println(gamesDesc[EATEN] + gamesPlayed[EATEN] + " games.");
        System.out.println(gamesDesc[FELL] + gamesPlayed[FELL] + " games.");
        System.out.println(gamesDesc[QUIT] + gamesPlayed[QUIT] + " games.");
    }

    //
    public void playOne() {
        // Print current location and available information
        System.out.print("You are in Room #" + bot.getCurrent() + "\n");
        System.out.println("To your left is #" + String.valueOf(bot.nextRoom('l')) + ", to your right is #" + String.valueOf(bot.nextRoom('r')) + ", and ahead is #" + String.valueOf(bot.nextRoom('a')));
        System.out.println("You have " + numArrows + " arrows remaining.");
    
        // Check if there's a smell of the ghost or a breeze from the pit
        if (bot.ghostNear()) {
            System.out.println("You can smell something horrible.");
        }
        if (bot.pitNear()) {
            System.out.println("You feel a cold wind.");
        }


        // Ask for user's move
        System.out.print("Please choose from (W)alk, (S)hoot, or (Q)uit: ");
        char move = sc.next().charAt(0);
        move = Character.toLowerCase(move);
        response = -1;
        
        // Implement user's move
        switch (move) {
            case 'w':
                System.out.print("Which room would you like to walk into? ");
                // Implement walking into another room
                int walkResult = bot.tryWalk(sc.nextInt());
                switch (walkResult) {
                    case 0:
                        gameOver = false;
                        response = 1;
                        break;
                    case 1:
                        gamesPlayed[EATEN]++;
                        gameOver = true;
                        response = 2;
                        break;
                    case 2:
                        gamesPlayed[FELL]++;
                        gameOver = true;
                        response = 3;
                        break;
                    default:
                        gameOver = false;
                        response = 0;
                        break;
                }
                break;
            case 's':
                System.out.print("Which room would you like to shoot into? ");
                // Implement shooting into another room
                if (numArrows > 0) {
                    numArrows--;
                    int shotResult = bot.tryShoot(sc.nextInt());
                    switch (shotResult) {
                        case 0:
                            gamesPlayed[WON]++;
                            gameOver = true;
                            response = 5;        
                            break;
                        case 3:
                            response = 6;
                            break;
                        default:
                            response = 4;
                            break;
                    }
                }
                else {
                    response = 7;
                }
                break;
            case 'q':
                // Quit the game
                gamesPlayed[QUIT]++;
                gameOver = true;
                break;
            default:
                System.out.println("Invalid move. Please try again.");
        }
        if(response >=0 && response <= 7)
            System.out.println(RESPONSES[response]);
    }

    public void explain() {
        trace("explain the game");
        System.out.println("Mystery Mansion Game!");
        System.out.println("================");
        System.out.println();
        System.out.println("You have to find and shoot the ghost and not fall in the pit.");
        System.out.println();
    }
    
    public void trace(String message) {
        if (tracing) {
            System.out.println("Mystery Mansion Game: " + message);
        }
    }



    public static void main(String[] args) {
        new MysteryMansionGame();
    }
}
