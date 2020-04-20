package com.kderyabin.scopes;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import de.saxsys.mvvmfx.Scope;
import lombok.ToString;
import org.springframework.stereotype.Service;


@Service
@ToString
public class BoardScope implements Scope {

    private BoardModel boardModel;
    private boolean hasBoards = false;
    private BoardItemModel itemModel;


    public BoardModel getBoardModel() {
        return boardModel;
    }

    public void setBoardModel(BoardModel boardModel) {
        this.boardModel = boardModel;
        if(boardModel != null){
            setHasBoards(true);
        }
    }

    public boolean isHasBoards() {
        return hasBoards;
    }

    public void setHasBoards(boolean hasBoards) {
        this.hasBoards = hasBoards;
    }

    public BoardItemModel getItemModel() {
        return itemModel;
    }

    public void setItemModel(BoardItemModel itemModel) {
        this.itemModel = itemModel;
    }
}
