package com.kderyabin.scopes;

import com.kderyabin.models.BoardModel;
import de.saxsys.mvvmfx.Scope;
import org.springframework.stereotype.Service;


@Service
public class BoardScope implements Scope {
    private BoardModel model;

    public BoardModel getModel() {
        return model;
    }

    public void setModel(BoardModel model) {
        this.model = model;
    }
}
