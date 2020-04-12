package com.kderyabin.services;

public interface NavigateServiceInterface {
    public void navigate(String viewName) throws Exception;

    boolean hasPrevious();

    void goToPrevious() throws Exception;
}
