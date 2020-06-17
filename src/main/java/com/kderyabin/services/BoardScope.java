package com.kderyabin.services;

import com.kderyabin.model.BoardItemModel;
import com.kderyabin.model.BoardModel;
import de.saxsys.mvvmfx.Scope;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;


@Service
@ToString
@Getter
@Setter
public class BoardScope implements Scope {

    /**
     * User selected board available in the current scope for updates.
     */
    private BoardModel boardModel;
    /**
     * Common for the whole app.
     * Set during the app bootstrap.
     * Must be updated whenever board is removed.
     */
    private boolean hasBoards = false;
    /**
     * Board item available for edition.
     * This property must be reset any time we (re)set a boardModel.
     */
    private BoardItemModel itemModel;

    /**
     * Resets some scoped data.
     */
    public void resetSelection(){
        boardModel = null;
        itemModel = null;
    }
}
