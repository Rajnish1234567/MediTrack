package com.airtribe.meditrack.interfaces;

public interface Searchable<T> {

    T searchById(int id);

    default boolean matches(String value, String keyword) {
        return value != null &&
               value.toLowerCase().contains(keyword.toLowerCase());
    }
}