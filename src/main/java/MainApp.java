import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MainApp {

    //棋盘边长
    int size;

    //棋盘
    Piece[][] board;

    //得分
    long score;

    //随机数生成器
    Random random = new Random();

    //是否游戏结束
    boolean isOver = false;

    //创建棋子
    public static Piece producePiece() {
        return new NumPiece();
    }
    public MainApp(int size) {
        this.size = size;
        board = new Piece[size][size];
    }

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(4);
        mainApp.play();
    }

    /**
     * 合并棋子，向左合并。2 2 0 0 --> 4 0 0 0
     * @param pieces
     */
    public void mergeRow(Piece[] pieces) {
        compressRow(pieces);
        for (int i = 0; i < pieces.length; i++) {
            if (i < 3) {
                Piece piece = pieces[i];
                Piece rightPiece = pieces[i+1];
                if (!piece.isBlank() && piece.equals(rightPiece)) {
                    score = score + piece.expand();
                    rightPiece.setBlank();
                    compressRow(pieces);
                }
            }
        }
    }

    /**
     * 压缩棋子，向左压缩，比如 2 0 0 2，会压缩为 2 2 0 0
     * @param pieces
     */
    public void compressRow(Piece[] pieces) {
        ArrayList<Piece> temp = new ArrayList<>();
        for (int i = 0; i < pieces.length; i++) {
            if (!pieces[i].isBlank()) {
                temp.add(pieces[i]);
            }
        }

        for (int i = 0; i < pieces.length; i++) {
            if (temp.size() > i) {
                pieces[i] = temp.get(i);
            } else {
                pieces[i] = producePiece();
            }
        }
    }

    public void play() {
        //重置得分
        score = 0;

        //重置棋盘
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = producePiece();
            }
        }

        //随机生成棋子
        this.randomOnePiece();
        this.randomOnePiece();

        //用户输入控制
        Scanner scanner = new Scanner(System.in);

        while (!isOver) { //判断游戏是否结束
            print(); //打印当前结果
            String key = scanner.next();
            System.out.println(key);
            if (this.directionMerge(key)) { //响应用户输入
                this.randomOnePiece();
                System.out.println("当前得分为：" + score);
            } else {
                System.out.println("您只可以输入[a、s、d、w]中的一个字符");
            }

        }
    }

    /**
     * 响应用户输入
     * @param key 输入的键盘字符
     *
     */
    public boolean directionMerge(String key) {
        //a--向左
        if (key.equals("a")) {
            for (int i = 0; i < size; i++) {
                Piece[] pieces = board[i];
                mergeRow(pieces);
            }
        }
        //d--向右
        else if (key.equals("d")) {
            for (int i = 0; i < size; i++) {
                Piece[] pieces = board[i];
                Piece[] npieces = new Piece[pieces.length];

                for (int j = 0; j < pieces.length; j++) {
                    npieces[npieces.length - 1 - j]  = pieces[j];
                }

                mergeRow(npieces);

                for (int j = 0; j < pieces.length; j++) {
                    pieces[j] = npieces[npieces.length - j - 1];
                }
            }
        }
        //w--向上
        else if (key.equals("w")) {
            for (int i = 0; i < size; i++) {
                Piece[] nPieces = new Piece[size];
                for (int j = 0; j < size; j++) {
                    nPieces[j] = board[j][i];
                }
                mergeRow(nPieces);

                for (int j = 0; j < size; j++) {
                    board[j][i] = nPieces[j];
                }
            }
        }
        //s--向下
        else if (key.equals("s")) {
            for (int i = 0; i < size; i++) {
                Piece[] nPieces = new Piece[size];
                for (int j = 0; j < size; j++) {
                    nPieces[j] = board[size - 1 - j][i];
                }
                mergeRow(nPieces);

                for (int j = 0; j < size; j++) {
                    board[size - j - 1][i] = nPieces[j];
                }
            }
        }
        else {
            return false;
        }


        return true;
    }


    /**
     * 随机生成一个棋子
     */
    public void randomOnePiece() {
        ArrayList<Piece> pieces = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isBlank()) {
                    pieces.add(board[i][j]);
                }
            }
        }

        if (pieces.size() == 0) {
            if (!canMerge()) { //没有空间，也没有能合并的棋子，游戏结束
                this.over();
            }
        } else {
            int i = random.nextInt(pieces.size());
            Piece piece = pieces.get(i);
            piece.init();

            //随机生成的棋子，其数值也按概率随机膨胀
            int randonInt = random.nextInt(100) + 1;

            if (randonInt > 85) {
                piece.expand();
            }
            if (randonInt > 95) {
                piece.expand();
            }
            if (randonInt > 98) {
                piece.expand();
            }
        }
    }

    /**
     * 判断当前棋盘是否还有合并机会
     * @return true：可以 false：不可以
     */
    public boolean canMerge() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (board[i][j].equals(board[i][j + 1]) || board[i][j].equals(board[i + 1][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 游戏结束
     */
    public void over() {
        System.out.println("不好意思，游戏结束");
        isOver = true;
    }

    /**
     * 向控制台打印棋盘及内容
     */
    public void print() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j]);
                System.out.print("\t");
            }

            System.out.println();
        }

    }

}
