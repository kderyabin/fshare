package com.kderyabin.scopes;

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
     * Board available in the current scope
     */
    private BoardModel boardModel;
    /**
     * Common for the app.
     */
    private boolean hasBoards = false;
    /**
     * Board item available for edition
     */
    private BoardItemModel itemModel;
}
