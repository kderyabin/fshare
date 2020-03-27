package com.kderyabin.views;

import com.kderyabin.viewmodels.BoardViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;

public class BoardFormView implements FxmlView<BoardViewModel> {

    @InjectViewModel
    private BoardViewModel viewModel;

    public void initialize() {

    }
}
