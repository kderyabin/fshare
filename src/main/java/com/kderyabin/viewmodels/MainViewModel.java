package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardModel;
import com.kderyabin.repository.BoardRepository;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ScopeProvider(BoardScope.class)
public class MainViewModel implements ViewModel {

    NavigateServiceInterface navigation;
    BoardRepository boardRepository;
    @InjectScope
    BoardScope scope;

    public void initialize(){
        // init recent boards
        try {
            List<BoardModel> recent = boardRepository.loadRecent(3);
            if(recent.size() > 0) {
                scope.setHasBoards(true);
                navigation.navigate("home");
            } else {
                navigation.navigate("start");
            }
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

    public NavigateServiceInterface getNavigate() {
        return navigation;
    }

    @Autowired
    public void setNavigate(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }
}
