package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardModel;
import com.kderyabin.repository.BoardRepository;
import com.kderyabin.scopes.BoardScope;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ScopeProvider(BoardScope.class)
public class MainViewModel implements ViewModel {

    BoardRepository boardRepository;
    @InjectScope
    BoardScope scope;
    private final BooleanProperty hasBoards = new SimpleBooleanProperty(false);

    public void initialize(){
        // init recent boards
        try {
            List<BoardModel> recent = boardRepository.loadRecent(3);
            scope.setHasBoards(recent.size() > 0);
            setHasBoards(recent.size() > 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public BoardRepository getBoardRepository() {
        return boardRepository;
    }

    @Autowired
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardScope getScope() {
        return scope;
    }

    @Autowired
    public void setScope(BoardScope scope) {
        this.scope = scope;
    }

    public boolean isHasBoards() {
        return hasBoards.get();
    }

    public BooleanProperty hasBoardsProperty() {
        return hasBoards;
    }

    public void setHasBoards(boolean hasBoards) {
        this.hasBoards.set(hasBoards);
    }
}
