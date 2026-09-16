package com.airtribe.meditrack.entity;

import java.util.ArrayList;
import java.util.List;

public class DataStore<T> {

    private final List<T> records = new ArrayList<>();

    public void add(T record) {
        records.add(record);
    }

    public boolean remove(T record) {
        return records.remove(record);
    }

    public List<T> getAll() {
        return new ArrayList<>(records);
    }

    public int size() {
        return records.size();
    }

    public void clear() {
        records.clear();
    }
}