package com.kderyabin.mvvm.main;

import com.kderyabin.partagecore.model.BoardModel;
import com.kderyabin.partagecore.storage.StorageManager;
import com.kderyabin.services.BoardScope;
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

    StorageManager storageManager;
    @InjectScope
    BoardScope scope;
    private final BooleanProperty hasBoards = new SimpleBooleanProperty(false);

    public void initialize(){
        // init recent boards
        try {
            List<BoardModel> recent =storageManager.getRecentBoards(3);
            scope.setHasBoards(recent.size() > 0);
            setHasBoards(recent.size() > 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
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
