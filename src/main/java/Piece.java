/**
 * 定义棋子接口
 */
public interface Piece {
    /**
     * 显示棋子内容
     * @return
     */
    @Override
    String toString();

    /**
     * 判断两个棋子内容是否相同
     * @param obj
     * @return
     */
    @Override
    boolean equals(Object obj);

    /**
     * 棋子合并时调用，如2、2合并为4
     * @return 返回得分
     */
    int expand();

    /**
     * 判断棋子是否为空
     * @return
     */
    boolean isBlank();

    /**
     * 把棋子设置为空
     */
    void setBlank();

    /**
     * 初始化棋子
     */
    void init();
}
