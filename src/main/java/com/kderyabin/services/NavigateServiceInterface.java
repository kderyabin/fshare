package com.kderyabin.services;

import com.kderyabin.error.ViewNotFoundException;

public interface NavigateServiceInterface {
    public void navigate(String viewName) throws ViewNotFoundException;
}
