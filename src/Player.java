public class Player {
    char[][] board;
    private int x;
    private int y;

    public Player(char[][] board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }
     public void walk(int input) {
        char playerNum = board[y][x];
        int oldX = x, oldY = y;
        board[x][y] = '.';
        switch (input) {
            case 1 -> y--;
            case 2 -> y++;
            case 3 -> x++;
            case 4 -> x--;
        }
        try{
            if (board[x][y] == '*') {
                System.out.format("%c is victorious!", playerNum);
            }

        } catch (Exception ignored) {
            System.out.println("Illegal move, your turn is over");
            // Revert deletion of player
            x = oldX;
            y = oldY;
        }
        finally{
            board[x][y] = playerNum;
        }
    }
}
