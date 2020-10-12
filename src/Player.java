import java.util.Scanner;

public class Player {
    char[][] board;
    private int x;
    private int y;
    private final char playerNum;
    private int numberOfWins;

    public char getPlayerNum() {
        return playerNum;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player(char[][] board, int x, int y, char playerNum) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.playerNum = playerNum;
        this.numberOfWins = 0;
    }
     public boolean walk(int direction) {
        int oldX = x, oldY = y;
        board[x][y] = '.';
        switch (direction) {
            case 1 -> y--;
            case 2 -> y++;
            case 3 -> x++;
            case 4 -> x--;
        }
        try{
            if (board[x][y] == '*') {
                numberOfWins++;
                return true;
            }
            else if (board[x][y] != '.'){
                throw new Exception("Illegal move!");
            }

        } catch (Exception e) {
            // Revert deletion of player
            System.out.println("Illegal move, your turn is over");
            x = oldX;
            y = oldY;
        }
        finally{
            board[x][y] = playerNum;
        }
         return false;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }


    boolean wantsToPlayAgain(Scanner stdin){
        while (true) {
            System.out.format("Player%c, would you like to keep playing?", playerNum);
            String answer = stdin.nextLine();
            if (!answer.equals("y") && !answer.equals("n")){
                System.out.println("Please enter y or n (for yes or no)");
                continue;
            }
            if (answer.equals("y")){
                return true;
            }
            else{
                return false;
            }
        }
    }

}
