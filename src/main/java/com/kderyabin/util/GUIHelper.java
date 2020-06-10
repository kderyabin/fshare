package com.kderyabin.util;

import javafx.scene.Node;
import javafx.util.StringConverter;

import java.util.Currency;
import java.util.Locale;

/**
 * Helper static methods to work with GUI
 */
final public class GUIHelper {

    /**
     * Renders a node visible or not visible.
     *
     * @param node    Element to hide/show
     * @param isVisible TRUE to set visible FALSE to hide
     */
    public static void renderVisible(Node node, boolean isVisible) {
        if (!isVisible) {
            // prevent size calculation
            node.setManaged(false);
            // hide
            node.setVisible(false);
            return;
        }
        node.setManaged(true);
        node.setVisible(true);
    }

    /**
     * Prepare Currency objects for display in combobox
     *
     * @return String converter for Currency object
     */
    public static StringConverter<Currency> getCurrencyStringConverter() {
        return new StringConverter<Currency>() {
            @Override
            public String toString(Currency object) {
                return String.format("%s (%s)", object.getDisplayName(), object.getCurrencyCode());
            }

            /**
             * Not used
             * @param string
             * @return null
             */
            @Override
            public Currency fromString(String string) {
                return null;
            }
        };
    }
    /**
     * Prepare Locale objects for display in combobox
     *
     * @return String converter for Locale object.
     */
    public static StringConverter<Locale> getLangStringConverter() {
        return new StringConverter<Locale>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayName();
            }

            /**
             * Not used
             * @param string
             * @return null
             */
            @Override
            public Locale fromString(String string) {
                return null;
            }
        };
    }

}
