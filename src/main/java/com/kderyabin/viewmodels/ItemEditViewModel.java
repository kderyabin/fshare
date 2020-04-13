package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@ScopeProvider(BoardScope.class)
public class ItemEditViewModel implements ViewModel {

    private NavigateServiceInterface navigation;
    private BoardItemModel model;
    @InjectScope
    private BoardScope scope;

    // Properties
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");

    public void initialize(){
        model = new BoardItemModel();
        model.setBoard(scope.getModel());
    }

    public void goBack() throws Exception {
        navigation.navigate("");
    }


    public boolean canGoBack() {
        return true;
    }
    public void save() {
    }

}
